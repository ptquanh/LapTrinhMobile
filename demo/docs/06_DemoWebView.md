# Tài Liệu Hướng Dẫn & Vấn Đáp: 06_DemoWebView

## 1. Tổng quan & Mục đích
- **Tên dự án:** `06_DemoWebView`
- **Mục đích:** Nhúng một trình duyệt web mini trực tiếp vào màn hình ứng dụng Android bằng `WebView` để hiển thị trang web trực tuyến từ URL hoặc nạp mã nguồn HTML/CSS/JS nội bộ.
- **Thành phần chính:**
  - `WebView`: Component hiển thị nội dung web.
  - Cấu hình quyền truy cập mạng trong `AndroidManifest.xml`.
  - `WebViewClient`: Xử lý điều hướng nạp trang ngay bên trong ứng dụng.

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. **Cấp quyền mạng:** Trong `AndroidManifest.xml`, phải khai báo `<uses-permission android:name="android.permission.INTERNET" />`.
2. **Cấu hình WebSettings:** Bật JavaScript thông qua `webView.getSettings().setJavaScriptEnabled(true)`.
3. **Chỉ định Client:** Gán `webView.setWebViewClient(new WebViewClient())` để ngăn hệ điều hành tự động bật trình duyệt ngoài (như Chrome, Samsung Internet).
4. **Tải trang:** Gọi `webView.loadUrl("https://google.com")`.
5. **Điều hướng lùi (Back Navigation):** Bắt sự kiện phím Back (`onBackPressed()`) để lùi trang web nếu `webView.canGoBack()` trước khi thoát Activity.

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Khai báo quyền (`AndroidManifest.xml`)
```xml
<manifest ...>
    <uses-permission android:name="android.permission.INTERNET" />
    <application ...>
        ...
    </application>
</manifest>
```

### 3.2 Logic Java (`MainActivity.java`)
```java
WebView webView = findViewById(R.id.webView);

// Cấu hình WebSettings
WebSettings settings = webView.getSettings();
settings.setJavaScriptEnabled(true);

// Bắt buộc để mở web ngay trong ứng dụng
webView.setWebViewClient(new WebViewClient());

// Tải địa chỉ trang web
webView.loadUrl("https://www.google.com");

// Xử lý nút Back của điện thoại
@Override
public void onBackPressed() {
    if (webView.canGoBack()) {
        webView.goBack(); // Quay lại trang trước trong lịch sử duyệt
    } else {
        super.onBackPressed(); // Thoát ứng dụng
    }
}
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Nếu không gọi lệnh `webView.setWebViewClient(new WebViewClient())`, điều gì sẽ xảy ra khi gọi `loadUrl()`?
- **Trả lời:**
  - Khi không set `WebViewClient`, hệ điều hành Android sẽ phát một Intent ngầm định (`ACTION_VIEW`) và mở trình duyệt mặc định bên ngoài của thiết bị (ví dụ Google Chrome) để tải URL đó, chứ không tải ngay bên trong ứng dụng của mình.

### Câu 2: Nếu ứng dụng cố tình tải một URL `http://` (không có S - cleartext) trên Android 9 (API 28) trở lên và bị lỗi màn hình trắng / net::ERR_CLEARTEXT_NOT_PERMITTED thì xử lý thế nào?
- **Trả lời:**
  - Từ Android 9, Google mặc định chặn toàn bộ kết nối không mã hóa HTTP để đảm bảo an toàn.
  - Để khắc phục, ta thêm thuộc tính `android:usesCleartextTraffic="true"` vào thẻ `<application>` trong file `AndroidManifest.xml` hoặc cấu hình file `network_security_config.xml`.

### Câu 3: Sự khác biệt giữa `WebViewClient` và `WebChromeClient` là gì?
- **Trả lời:**
  - `WebViewClient`: Quản lý các sự kiện nạp trang, chuyển hướng URL, bắt lỗi mạng (`onReceivedError`), và xác thực SSL.
  - `WebChromeClient`: Quản lý các chức năng tương tác giao diện người dùng cao cấp của trình duyệt như thanh tiến trình tải (`onProgressChanged`), tiêu đề trang web (`onReceivedTitle`), icon favicon, và các hộp thoại JavaScript (`alert()`, `confirm()`, quyền truy cập camera/micro).
