# Tài Liệu Hướng Dẫn & Vấn Đáp: 15_DemoWeather

## 1. Tổng quan & Mục đích

- **Tên dự án:** `15_DemoWeather`
- **Tài liệu gốc:** Bài học 7 (`DemoWeatherAPI.pdf`, `Layout_Demo_Netwwork_Android.pdf`, `ic_baseline_search_24.xml`) và dự án thực hành `NetworkingAndroid`.
- **Mục đích:** Xây dựng ứng dụng tra cứu thời tiết hiện tại và dự báo 24 giờ trong ngày theo tên thành phố thông qua RESTful API (`api.weatherapi.com`), hiển thị lên giao diện chính và danh sách `ListView` tùy chỉnh.
- **Thành phần kỹ thuật trọng tâm:**
  - **Volley (`com.android.volley:volley:1.2.1`):** Tạo hàng đợi `RequestQueue` và gửi HTTP `GET` request bất đồng bộ bằng `JsonObjectRequest`.
  - **Picasso (`com.squareup.picasso:picasso:2.8`):** Tải ảnh icon thời tiết bất đồng bộ từ URL mạng (`"https:" + icon`) và gắn trực tiếp vào `ImageView`.
  - **JSON Parsing (`org.json`):** Bóc tách cấu trúc lồng nhau `JSONObject` (`current`, `condition`, `forecast`) và `JSONArray` (`forecastday` → `hour`).
  - **Custom `ListView` (`WeatherAdapter` & `WeatherForecastModel`):** Kế thừa `BaseAdapter`, tái sử dụng dòng giao diện qua kiểm tra `if (view == null)` để hiển thị 24 khung giờ dự báo.

---

## 2. Luồng hoạt động (Operational Flow)

```
[Khởi chạy MainActivity]
       ↓
[Ánh xạ View (txtCityName, editTextTextPersonName, imgSearch, txtTemperature, imgSeasonForecast, txtCityTemperature, lvWeatherForecast)]
       ↓
[Khởi tạo weatherForecastModelList + WeatherAdapter và gắn vào lvWeatherForecast]
       ↓
[Gọi mặc định GetData("Hanoi") (hoặc khi người dùng nhập tên TP & bấm imgSearch)]
       ↓
[Cập nhật txtCityName -> Xóa danh sách cũ weatherForecastModelList.clear()]
       ↓
[Tạo RequestQueue & đưa JsonObjectRequest(GET, url) vào hàng đợi]
       ↓
[onResponse(JSONObject response) trả về trên Main Thread]
       ├── Đọc current.temp_c -> gán vào txtTemperature
       ├── Đọc current.condition.text & icon -> gán txtCityTemperature + Picasso tải ảnh vào imgSeasonForecast
       └── Duyệt mảng forecast.forecastday[0].hour (24 phần tử)
              ↓
           Thêm từng đối tượng WeatherForecastModel(time, temper, img, wind) vào list
              ↓
           Gọi weatherAdapter.notifyDataSetChanged() để cập nhật ListView
```

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Quyền truy cập mạng trong `AndroidManifest.xml` & Thư viện trong `app/build.gradle`

- Khai báo quyền truy cập Internet trong `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
- Khai báo 2 thư viện mạng và tải ảnh:
```groovy
implementation 'com.android.volley:volley:1.2.1'
implementation 'com.squareup.picasso:picasso:2.8'
```

### 3.2 Cấu trúc Layout (`activity_main.xml` & `layout_weather_items.xml`)

- **`activity_main.xml`:** Dùng `LinearLayout` dọc (`vertical`) sắp xếp lần lượt từ trên xuống:
  1. `txtCityName` (`30sp`, in đậm, căn giữa) hiển thị tên thành phố.
  2. `LinearLayout` ngang chứa ô nhập `editTextTextPersonName` (`layout_weight="3"`, hint `"Enter City"`) và nút kính lúp `imgSearch` (`48dp x 48dp`, `@drawable/ic_baseline_search_24`).
  3. `txtTemperature` (`80dp`, in đậm) hiển thị nhiệt độ hiện tại.
  4. `imgSeasonForecast` (`60dp x 60dp`) và `txtCityTemperature` hiển thị biểu tượng & trạng thái thời tiết hiện tại.
  5. `lvWeatherForecast` hiển thị danh sách dự báo theo từng giờ.
- **`layout_weather_items.xml`:** Mỗi dòng chia làm 2 nửa bằng `layout_weight="1"`:
  - Nửa trái (`horizontal`): `txtTimeForecast` (`100dp x 100dp`) + `imgForecast` (`100dp x 100dp`).
  - Nửa phải (`vertical`): `txtTemperatureForecast` (`50dp` cao) ở trên + `txtWindForecast` (`50dp` cao) ở dưới.

### 3.3 Cài đặt `WeatherAdapter` (`WeatherAdapter.java`)

```java
@Override
public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_weather_items, null);
    }
    TextView txtTimeForecast = view.findViewById(R.id.txtTimeForecast);
    TextView txtTemperatureForecast = view.findViewById(R.id.txtTemperatureForecast);
    TextView txtWindForecast = view.findViewById(R.id.txtWindForecast);
    ImageView imgForecast = view.findViewById(R.id.imgForecast);

    WeatherForecastModel weatherForecastModel = weatherForecastModelList.get(i);
    txtTimeForecast.setText("" + weatherForecastModel.getTime());
    txtTemperatureForecast.setText(weatherForecastModel.getTemperature() + "°C");
    txtWindForecast.setText(weatherForecastModel.getWindSpeed() + " km");

    Picasso.get().load("https:" + weatherForecastModel.getIcon()).into(imgForecast);

    return view;
}
```

### 3.4 Hàm `GetData` gọi API bằng Volley (`MainActivity.java`)

```java
private void GetData(String cityName) {
    String url = "https://api.weatherapi.com/v1/forecast.json?key=fc8f4188ad3f48d8a10132707221212&q="
            + cityName + "&days=1&aqi=no&alerts=no";
    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
    Log.d("BBB", url.toString());
    txtCityName.setText(cityName);
    weatherForecastModelList.clear();

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String temperature = response.getJSONObject("current").getString("temp_c");
                        txtTemperature.setText(temperature + "°C");
                        String condition = response.getJSONObject("current")
                                .getJSONObject("condition").getString("text");
                        String conditionicon = response.getJSONObject("current")
                                .getJSONObject("condition").getString("icon");
                        txtCityTemperature.setText(condition);

                        Picasso.get().load("https:" + conditionicon).into(imgSeasonForecast);

                        JSONObject forecastObj = response.getJSONObject("forecast");
                        JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                        JSONArray hourArray = forecast0.getJSONArray("hour");
                        for (int i = 0; i < hourArray.length(); i++) {
                            JSONObject hourObj = hourArray.getJSONObject(i);
                            String time = hourObj.getString("time");
                            String temper = hourObj.getString("temp_c");
                            String img = hourObj.getJSONObject("condition").getString("icon");
                            String wind = hourObj.getString("wind_kph");
                            weatherForecastModelList.add(new WeatherForecastModel(time, temper, img, wind));
                        }
                        weatherAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    });
    requestQueue.add(jsonObjectRequest);
}
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Tại sao trong Android không được thực hiện gọi HTTP API trực tiếp trên Main Thread (UI Thread) mà phải dùng thư viện như Volley?

- **Trả lời:**
  - Main Thread chịu trách nhiệm vẽ giao diện và phản hồi tương tác người dùng. Tác vụ mạng có độ trễ không xác định nên nếu chạy trên Main Thread sẽ gây đơ màn hình và dẫn đến lỗi **ANR (Application Not Responding)**.
  - Từ Android 3.0 (API 11) trở đi, nếu mở kết nối mạng trực tiếp trên Main Thread, hệ điều hành sẽ ném ngoại lệ **`NetworkOnMainThreadException`**.
  - **Volley** tự động gửi request ở Background Thread và trả kết quả `JSONObject` về lại **Main Thread** bên trong `onResponse()` để cập nhật `TextView`, `ListView` an toàn.

### Câu 2: Tại sao phải nối chuỗi `"https:"` vào trước `conditionicon` và `weatherForecastModel.getIcon()` khi gọi Picasso?

- **Trả lời:**
  - JSON trả về từ WeatherAPI chứa đường dẫn tương đối giao thức dạng `"//cdn.weatherapi.com/weather/64x64/day/116.png"` (thiếu `https:`).
  - Khi nối `"https:" + conditionicon`, chuỗi trở thành URL hoàn chỉnh `"https://cdn.weatherapi.com/..."` để Picasso tải được ảnh.

### Câu 3: Tại sao phải gọi `weatherForecastModelList.clear()` trước khi nạp dữ liệu mới và gọi `weatherAdapter.notifyDataSetChanged()` sau vòng lặp `for`?

- **Trả lời:**
  - `weatherForecastModelList.clear()` giúp xóa sạch 24 khung giờ của thành phố cũ; nếu quên gọi, dữ liệu thành phố mới sẽ bị nối tiếp vào đuôi danh sách thành phố cũ (48, 72 dòng...).
  - `weatherAdapter.notifyDataSetChanged()` phải đặt bên trong `onResponse()` (sau khi vòng lặp `for` đã thêm đủ dữ liệu vào list) vì Volley chạy **bất đồng bộ**. Nếu đặt dòng này ngay bên ngoài sau `GetData(...)` (như dòng comment trong `onClick`), thời điểm đó dữ liệu từ server chưa kịp trả về nên `ListView` sẽ không cập nhật.

### Câu 4: Thư viện `Picasso` giải quyết những vấn đề gì khi tải ảnh trong `ListView`?

- **Trả lời:**
  1. **Tải ảnh bất đồng bộ ở luồng nền**, không làm giật lag khi cuộn `ListView`.
  2. **Tự động quản lý tái sử dụng View (`convertView`):** Hủy hoặc khớp đúng request ảnh cho từng dòng khi cuộn nhanh, tránh lỗi hiển thị nhầm icon của giờ khác.
  3. **Tự động lưu bộ nhớ đệm (Memory & Disk Cache):** Các khung giờ có cùng icon thời tiết không phải tải đi tải lại nhiều lần từ Internet.
