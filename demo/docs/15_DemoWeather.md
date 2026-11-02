# Tài Liệu Hướng Dẫn & Vấn Đáp: 15_DemoWeather

## 1. Tổng quan & Mục đích

- **Tên dự án:** `15_DemoWeather`
- **Mục đích:** Xây dựng ứng dụng tra cứu thời tiết hiện tại và dự báo 24 giờ trong ngày theo tên thành phố thông qua RESTful API (`api.weatherapi.com`), kết hợp tải ảnh bất đồng bộ và theo dõi trạng thái kết nối mạng thời gian thực bằng Dynamic `BroadcastReceiver`.
- **Thành phần kỹ thuật trọng tâm:**
  - **Volley (`com.android.volley:volley:1.2.1`):** Gửi HTTP `GET` request bất đồng bộ (`JsonObjectRequest`) và quản lý hàng đợi `RequestQueue`.
  - **Picasso (`com.squareup.picasso:picasso:2.8`):** Tải, lưu đệm (cache) và hiển thị icon thời tiết từ đường dẫn mạng lên `ImageView`.
  - **JSON Parsing (`org.json`):** Bóc tách cấu trúc lồng nhau `JSONObject` (`location`, `current`, `condition`, `forecast`) và `JSONArray` (`forecastday` → `hour`).
  - **Custom `ListView` (`WeatherAdapter`):** Kế thừa `BaseAdapter`, tái sử dụng dòng giao diện qua kiểm tra `if (view == null)` đúng theo mẫu bài giảng trên lớp để hiển thị 24 khung giờ.
  - **Dynamic `BroadcastReceiver` (`NetworkChangeReceiver`):** Đăng ký động theo vòng đời `onStart()` / `onStop()` (hỗ trợ cờ bảo mật `Context.RECEIVER_NOT_EXPORTED` trên Android 13+) để phát hiện mất mạng / có mạng trở lại và tự động cập nhật lại dữ liệu.
  - **Fallback an toàn (`WeatherFallbackProvider`):** Cung cấp dữ liệu mẫu dự phòng khi mất mạng hoặc khi API key hết hạn/vượt giới hạn để không bao giờ làm đơ giao diện khi demo.

---

## 2. Luồng hoạt động (Operational Flow)

```
[Khởi chạy MainActivity]
       ↓
[Khởi tạo View + WeatherAdapter + RequestQueue dùng chung]
       ↓
[onStart(): Đăng ký động NetworkChangeReceiver lắng nghe CONNECTIVITY_ACTION]
       ↓
[Gọi GetData("Hanoi") hoặc khi người dùng nhập tên thành phố & bấm Tìm kiếm]
       ↓
  ┌────┴───────────────────────────────────────────────────┐
  ↓ (Có kết nối Internet)                                  ↓ (Không có mạng)
[Tạo URL có mã hóa tên thành phố (URLEncoder)]      [Đổi banner sang "Mất kết nối mạng"]
       ↓                                                   ↓
[Đưa JsonObjectRequest vào RequestQueue]            [Nạp 24h dữ liệu mẫu dự phòng]
       ↓                                                   ↓
  ┌────┴─────────────────────────┐                  [Khi bật lại WiFi/4G → Receiver báo]
  ↓ (onResponse 200 OK)          ↓ (onErrorResponse)       ↓
[Bóc tách JSON current + 24h]  [400: Báo sai tên TP] [Tự động gọi lại GetData(city)]
  ↓                            [401/403/5xx: Nạp mẫu dự phòng]
[Picasso tải icon "https:" + iconUrl]
  ↓
[weatherAdapter.notifyDataSetChanged() cập nhật ListView]
```

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Quyền truy cập trong `AndroidManifest.xml`

Ứng dụng làm việc với API mạng và kiểm tra trạng thái kết nối bắt buộc phải khai báo 2 quyền:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

> Nếu thiếu `INTERNET`, Volley và Picasso không thể mở socket kết nối ra ngoài. Nếu thiếu `ACCESS_NETWORK_STATE`, `ConnectivityManager` sẽ ném `SecurityException`.

### 3.2 Gọi REST API bằng Volley và bóc tách JSON (`MainActivity.java`)

```java
String url = WeatherFallbackProvider.buildForecastUrl(
        WeatherFallbackProvider.DEFAULT_API_KEY,
        currentCityQuery
);

JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        this::handleWeatherResponse,
        error -> handleWeatherError(error, currentCityQuery)
);
jsonObjectRequest.setTag(REQUEST_TAG);
requestQueue.add(jsonObjectRequest);
```

- **Tại sao khởi tạo `RequestQueue` một lần duy nhất trong `onCreate()` thay vì tạo mới mỗi lần bấm nút?**
  - Mỗi `Volley.newRequestQueue()` sinh ra một cụm luồng nền (`CacheDispatcher` + 4 `NetworkDispatcher`). Nếu tạo mới liên tục trong hàm `GetData()`, ứng dụng sẽ rò rỉ luồng (Thread leak) và tốn bộ nhớ.
- **Tại sao phải mã hóa tên thành phố (`URLEncoder.encode`)?**
  - Với tên thành phố có khoảng trắng như `"Ho Chi Minh"` hay `"Da Nang"`, khoảng trắng thô trong URL có thể gây lỗi cú pháp URI; mã hóa thành `Ho%20Chi%20Minh` giúp truy vấn chính xác tuyệt đối.
- **Tại sao phải nối tiền tố `"https:"` vào trường `icon` của WeatherAPI?**
  - JSON từ WeatherAPI trả về đường dẫn tương đối giao thức dạng `"//cdn.weatherapi.com/weather/64x64/day/116.png"`. Khi ghép `"https:" + conditionIcon`, Picasso mới nhận được URL hợp lệ `"https://cdn.weatherapi.com/..."`.

### 3.3 Cài đặt `WeatherAdapter` tái sử dụng View chuẩn bài giảng (`WeatherAdapter.java`)

```java
@Override
public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null) {
        view = LayoutInflater.from(context)
                .inflate(R.layout.layout_weather_items, viewGroup, false);
    }

    TextView txtTimeForecast = view.findViewById(R.id.txtTimeForecast);
    TextView txtTemperatureForecast = view.findViewById(R.id.txtTemperatureForecast);
    TextView txtWindForecast = view.findViewById(R.id.txtWindForecast);
    ImageView imgForecast = view.findViewById(R.id.imgForecast);

    WeatherForecastModel weatherForecastModel = weatherForecastModelList.get(i);
    txtTimeForecast.setText(formatDisplayTime(weatherForecastModel.getTime()));
    txtTemperatureForecast.setText(weatherForecastModel.getTemperature() + "°C");
    txtWindForecast.setText("Gió: " + weatherForecastModel.getWindSpeed() + " km/h");

    String iconUrl = weatherForecastModel.getFullIconUrl();
    if (iconUrl != null) {
        Picasso.get()
                .load(iconUrl)
                .placeholder(R.drawable.ic_weather_cloud_sun)
                .error(R.drawable.ic_weather_cloud_sun)
                .into(imgForecast);
    } else {
        imgForecast.setImageResource(R.drawable.ic_weather_cloud_sun);
    }
    return view;
}
```

### 3.4 Đăng ký động `NetworkChangeReceiver` theo vòng đời Activity

```java
@Override
protected void onStart() {
    super.onStart();
    if (!isReceiverRegistered) {
        IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(networkReceiver, networkFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(networkReceiver, networkFilter);
        }
        isReceiverRegistered = true;
    }
}

@Override
protected void onStop() {
    super.onStop();
    if (isReceiverRegistered) {
        unregisterReceiver(networkReceiver);
        isReceiverRegistered = false;
    }
}
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Tại sao trong Android không được thực hiện gọi HTTP API trực tiếp trên Main Thread (UI Thread) mà phải dùng thư viện như Volley?

- **Trả lời:**
  - Main Thread chịu trách nhiệm vẽ giao diện ở tốc độ 60–120 khung hình/giây (mỗi khung hình chỉ có ~16ms). Tác vụ mạng có độ trễ không xác định (vài trăm ms đến vài giây).
  - Từ Android 3.0 (API 11) trở đi, nếu mở kết nối mạng trực tiếp trên Main Thread, hệ thống sẽ lập tức ném ngoại lệ **`NetworkOnMainThreadException`** để ngăn chặn lỗi treo ứng dụng **ANR (Application Not Responding)**.
  - **Volley** tự động thực thi kết nối HTTP và đọc luồng dữ liệu trên Background Thread, sau đó tự động chuyển kết quả JSON về lại **Main Thread** trong hàm `onResponse()` để ta cập nhật `TextView` / `ListView` an toàn.

### Câu 2: Thư viện `Picasso` giải quyết những bài toán khó nào nếu so với việc tự dùng `HttpURLConnection` + `BitmapFactory.decodeStream()` để tải ảnh trong `ListView`?

- **Trả lời:**
  1. **Bất đồng bộ & Quản lý Thread Pool:** Tự tải ảnh ở luồng nền, không làm giật cuộn `ListView`.
  2. **Tự động hủy/khớp lại yêu cầu khi tái chế View (`convertView`):** Trong `ListView`, một dòng vừa cuộn khuất được tái sử dụng ngay cho dòng mới. Nếu tự viết luồng tải ảnh thô, ảnh của giờ cũ tải xong muộn sẽ đè nhầm lên dòng của giờ mới (hiện tượng nhảy/nhấp nháy sai ảnh). Picasso tự động hủy request cũ gắn với `ImageView` đó.
  3. **Bộ nhớ đệm 2 tầng (Memory LruCache + Disk Cache):** Các icon thời tiết giống nhau (ví dụ cùng biểu tượng có mây ở nhiều khung giờ) chỉ tải từ mạng 1 lần duy nhất rồi lấy tức thì từ RAM, tiết kiệm băng thông và tránh lỗi tràn bộ nhớ `OutOfMemoryError`.

### Câu 3: Tại sao `NetworkChangeReceiver` lại được đăng ký động (`registerReceiver`) trong `onStart()` và hủy trong `onStop()`, thay vì khai báo tĩnh trong `AndroidManifest.xml`?

- **Trả lời:**
  1. **Hạn chế của Android mới (API 24+ / Android 7.0+):** Google đã chặn không cho các ứng dụng nhận broadcast ngầm `CONNECTIVITY_ACTION` qua khai báo tĩnh trong `AndroidManifest.xml` nhằm tránh hàng chục app cùng thức dậy gây hao pin khi chuyển WiFi/4G. Chỉ `BroadcastReceiver` đăng ký động khi ứng dụng đang chạy mới nhận được sự kiện này.
  2. **Quản lý tài nguyên theo vòng đời:** Đăng ký ở `onStart()` (khi màn hình hiển thị) và hủy ở `onStop()` (khi người dùng thoát/ẩn màn hình) giúp tránh rò rỉ bộ nhớ (**Receiver Leak** — lỗi `IntentReceiverLeaked`) và không cập nhật giao diện khi Activity đã khuất.

### Câu 4: Cờ `Context.RECEIVER_NOT_EXPORTED` khi gọi `registerReceiver` trên Android 13+ (API 33 / Tiramisu) có ý nghĩa gì?

- **Trả lời:**
  - Từ Android 13 (và bắt buộc nghiêm ngặt trên Android 14+), khi đăng ký động một `BroadcastReceiver` không dành riêng cho broadcast hệ thống thuần túy, lập trình viên bắt buộc phải chỉ định rõ phạm vi tiếp nhận (`RECEIVER_EXPORTED` hoặc `RECEIVER_NOT_EXPORTED`), nếu không hệ thống sẽ ném `SecurityException`.
  - `RECEIVER_NOT_EXPORTED` đảm bảo các ứng dụng bên thứ ba khác trên máy không thể giả mạo gửi Intent giả vào Receiver của ứng dụng mình, tăng cường bảo mật.

### Câu 5: Sự khác biệt giữa lỗi HTTP `400 Bad Request` và lỗi `401/403` hoặc mất mạng khi tra cứu WeatherAPI được xử lý như thế nào trong bài?

- **Trả lời:**
  - **HTTP `400`:** Do người dùng gõ tên thành phố không tồn tại (ví dụ `"asdfgh"`). Ứng dụng giữ nguyên dữ liệu thành phố hợp lệ đang xem và hiện `Toast` nhắc kiểm tra lại tên thành phố.
  - **Mất mạng hoặc HTTP `401/403` (hết hạn/quá hạn mức API key):** Ứng dụng tự động chuyển sang bộ dữ liệu mẫu dự phòng 24 giờ (`WeatherFallbackProvider`), cập nhật nhãn trạng thái để người dùng biết đang xem dữ liệu dự phòng, và tự động gọi lại API thật ngay khi `NetworkChangeReceiver` phát hiện có mạng trở lại.
