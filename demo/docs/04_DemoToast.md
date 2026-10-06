# Tài Liệu Hướng Dẫn & Vấn Đáp: 04_DemoToast

## 1. Tổng quan & Mục đích
- **Tên dự án:** `04_DemoToast`
- **Mục đích:** Tìm hiểu cách hiển thị thông báo tạm thời không gây gián đoạn thao tác người dùng (`Toast`) ở chế độ tiêu chuẩn và chế độ tùy chỉnh giao diện (Custom Toast).
- **Thành phần chính:**
  - `Toast` mặc định: Sử dụng phương thức `Toast.makeText()`.
  - Nút bấm kích hoạt: `btnShortToast`, `btnLongToast`, `btnCustomToast`.

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. **Tạo Toast:** Gọi phương thức tĩnh `Toast.makeText(Context context, CharSequence text, int duration)`.
2. **Thời lượng:**
   - `Toast.LENGTH_SHORT`: Hiển thị khoảng 2 giây.
   - `Toast.LENGTH_LONG`: Hiển thị khoảng 3.5 giây.
3. **Hiển thị:** Bắt buộc phải gọi lệnh `.show()` thì Toast mới được đẩy lên hàng đợi `NotificationManagerService` của hệ điều hành để vẽ ra màn hình.
4. **Tự động biến mất:** Sau khi hết thời lượng, Toast tự biến mất mà không yêu cầu người dùng phải chạm hay đóng.

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Toast mặc định (`MainActivity.java`)
```java
// Cách 1: Chuỗi lệnh ngắn gọn
Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

// Cách 2: Khởi tạo đối tượng và tùy biến vị trí
Toast toast = Toast.makeText(getApplicationContext(), "Lưu dữ liệu thành công", Toast.LENGTH_LONG);
toast.setGravity(Gravity.CENTER, 0, 0); // Đặt giữa màn hình
toast.show();
```

### 3.2 Custom Toast (Giao diện tùy chỉnh)
```java
LayoutInflater inflater = getLayoutInflater();
View customView = inflater.inflate(R.layout.custom_toast_layout, null);
TextView tvToast = customView.findViewById(R.id.tvCustomToast);
tvToast.setText("Thông báo tùy biến đẹp!");

Toast customToast = new Toast(getApplicationContext());
customToast.setDuration(Toast.LENGTH_LONG);
customToast.setView(customView);
customToast.show();
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Nếu quên gọi hàm `.show()` sau khi tạo `Toast.makeText()`, điều gì sẽ xảy ra?
- **Trả lời:**
  - Đối tượng `Toast` chỉ được khởi tạo trong bộ nhớ RAM nhưng không hề gửi thông điệp hiển thị tới `NotificationManagerService` của hệ điều hành. Do đó không có bất kỳ thông báo nào xuất hiện trên màn hình, và chương trình cũng không bị văng (không crash).

### Câu 2: Sự khác nhau giữa `Toast` và `Snackbar` là gì? Khi nào nên dùng loại nào?
- **Trả lời:**
  - `Toast`: Không gắn liền với View hierarchy của Activity, có thể tiếp tục hiển thị ngay cả khi Activity đó đã bị đóng hoặc chuyển app. Toast không thể chứa nút bấm tương tác (Action button).
  - `Snackbar`: Gắn liền với một View/CoordinatorLayout cụ thể của Activity, có thể đính kèm nút hành động (như nút "Undo", "Thử lại"), và tự động tương tác với các thành phần như `FloatingActionButton` (đẩy FAB lên khi xuất hiện).

### Câu 3: Từ Android 11 (API 30) trở lên, Google đã hạn chế Custom Toast như thế nào?
- **Trả lời:**
  - Google đã deprecate phương thức `Toast.setView()` khi ứng dụng chạy ngầm (background) hoặc trên các phiên bản Android mới để ngăn chặn việc giả mạo giao diện hệ thống hoặc lừa đảo (phishing). Google khuyến cáo chuyển sang dùng `Snackbar` hoặc Notification cho các giao diện thông báo phức tạp.
