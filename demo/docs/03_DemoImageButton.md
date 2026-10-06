# Tài Liệu Hướng Dẫn & Vấn Đáp: 03_DemoImageButton

## 1. Tổng quan & Mục đích
- **Tên dự án:** `03_DemoImageButton`
- **Mục đích:** Tìm hiểu cách sử dụng nút bấm dạng hình ảnh (`ImageButton`) thay vì nút bấm chữ tiêu chuẩn (`Button`), xử lý sự kiện click để thay đổi trạng thái icon hoặc thực thi logic nghiệp vụ.
- **Thành phần chính:**
  - `ImageButton`: Nút bấm nhận ảnh qua thuộc tính `src` hoặc `srcCompat`.
  - `TextView`: Hiển thị thông báo khi nút được nhấn.

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. Khởi tạo Activity, nạp layout chứa `ImageButton`.
2. Gán sự kiện `setOnClickListener` cho `ImageButton`.
3. Khi người dùng bấm vào nút:
   - Hệ thống kích hoạt phương thức `onClick(View v)`.
   - Có thể thay đổi ảnh động bằng `setImageResource(R.drawable.new_image)`.
   - Hiển thị phản hồi thị giác (Feedback Toast hoặc TextView).

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Giao diện XML (`activity_main.xml`)
```xml
<ImageButton
    android:id="@+id/btnImage"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/ic_launcher_foreground"
    android:contentDescription="Nút biểu tượng"
    android:background="?attr/selectableItemBackgroundBorderless" />
```
- `android:contentDescription`: Cung cấp mô tả ngữ nghĩa trợ năng cho người khiếm thị (Accessibility / TalkBack).
- `android:background`: Đổi thành hiệu ứng gợn sóng (ripple) không viền thay vì viền xám mặc định.

### 3.2 Logic Java (`MainActivity.java`)
```java
ImageButton btnImage = findViewById(R.id.btnImage);
btnImage.setOnClickListener(new View.OnClickListener() {
    private boolean isTurnedOn = false;
    @Override
    public void onClick(View v) {
        isTurnedOn = !isTurnedOn;
        if (isTurnedOn) {
            btnImage.setImageResource(R.drawable.ic_on);
        } else {
            btnImage.setImageResource(R.drawable.ic_off);
        }
    }
});
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Sự khác nhau cơ bản giữa `Button` và `ImageButton` là gì?
- **Trả lời:**
  - `Button` kế thừa trực tiếp từ `TextView`, được thiết kế tối ưu để hiển thị văn bản (text), dù vẫn có thể gắn drawable kèm chữ (`drawableLeft`, `drawableTop`).
  - `ImageButton` kế thừa từ `ImageView`, chuyên dụng để hiển thị một hình ảnh duy nhất và không thể chứa văn bản thuần trực tiếp bên trong.

### Câu 2: Thuộc tính `android:contentDescription` trên `ImageButton` có bắt buộc không? Nếu không khai báo thì Android Studio sẽ báo gì?
- **Trả lời:**
  - Không bắt buộc ứng dụng phải chạy được (không gây crash), nhưng Android Lint sẽ phát ra cảnh báo màu vàng (Warning).
  - Mục đích của thuộc tính này là hỗ trợ người dùng khiếm thị khi họ sử dụng tính năng TalkBack (trợ năng màn hình), hệ điều hành sẽ đọc tên mô tả của nút bấm này lên để người dùng biết chức năng của nó.

### Câu 3: Làm sao để loại bỏ phần nền viền xám mặc định của `ImageButton`?
- **Trả lời:**
  - Đặt thuộc tính `android:background="@null"` hoặc `android:background="@android:color/transparent"`.
  - Hoặc dùng hiệu ứng gợn sóng Material: `android:background="?attr/selectableItemBackgroundBorderless"`.
