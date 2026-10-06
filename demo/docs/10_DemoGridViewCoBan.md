# Tài Liệu Hướng Dẫn & Vấn Đáp: 10_DemoGridViewCoBan

## 1. Tổng quan & Mục đích
- **Tên dự án:** `10_DemoGridViewCoBan`
- **Mục đích:** Tìm hiểu cách tổ chức và hiển thị dữ liệu dưới dạng lưới đa chiều (hàng và cột) bằng `GridView` cơ bản kết hợp `ArrayAdapter`.
- **Thành phần chính:**
  - `GridView`: View hiển thị danh sách cuộn theo dạng ma trận nhiều cột.
  - Nguồn dữ liệu mảng chuỗi `String[]` hoặc danh sách ký tự/chữ cái.
  - Thuộc tính cấu hình lưới: `numColumns`, `horizontalSpacing`, `verticalSpacing`.

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. Trong layout XML, định nghĩa thẻ `GridView` và đặt số cột hiển thị cố định hoặc tự động (`auto_fit`).
2. Trong Java, khởi tạo dữ liệu danh sách chuỗi.
3. Sử dụng `ArrayAdapter` để gắn dữ liệu vào `GridView`.
4. Bắt sự kiện khi người dùng click vào một ô trong lưới qua `setOnItemClickListener`.

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Giao diện XML (`activity_main.xml`)
```xml
<GridView
    android:id="@+id/gridView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:numColumns="3"
    android:verticalSpacing="8dp"
    android:horizontalSpacing="8dp"
    android:gravity="center"
    android:stretchMode="columnWidth" />
```
- `android:numColumns="3"`: Quy định hiển thị 3 cột trên một hàng.
- `android:horizontalSpacing` / `verticalSpacing`: Khoảng cách lề giữa các cột và các hàng.
- `android:stretchMode="columnWidth"`: Tự động kéo giãn độ rộng của các cột để lấp đầy không gian trống còn lại của màn hình.

### 3.2 Logic Java (`MainActivity.java`)
```java
GridView gridView = findViewById(R.id.gridView);

String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
ArrayAdapter<String> adapter = new ArrayAdapter<>(
    this,
    android.R.layout.simple_list_item_1,
    data
);

gridView.setAdapter(adapter);

gridView.setOnItemClickListener((parent, view, position, id) -> {
    Toast.makeText(MainActivity.this, "Ô số: " + position + " - Giá trị: " + data[position], Toast.LENGTH_SHORT).show();
});
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: `GridView` khác gì so với `ListView`? Khi nào nên dùng loại nào?
- **Trả lời:**
  - `ListView` chỉ hiển thị danh sách theo 1 chiều duy nhất (1 cột nhiều dòng dọc). Phù hợp cho danh sách tin nhắn, danh bạ, cài đặt hệ thống.
  - `GridView` hiển thị dữ liệu theo dạng lưới ma trận 2 chiều (nhiều cột và nhiều dòng). Phù hợp cho thư viện ảnh (gallery), danh mục sản phẩm thương mại điện tử, bàn phím số/máy tính bỏ túi.

### Câu 2: Nếu muốn `GridView` tự động tính toán số cột dựa trên kích thước màn hình thiết bị (điện thoại vs máy tính bảng) thì làm thế nào?
- **Trả lời:**
  - Ta đặt thuộc tính `android:numColumns="auto_fit"` kết hợp với việc định nghĩa chiều rộng cố định tối thiểu cho mỗi cột bằng `android:columnWidth="100dp"`.
  - Khi đó, hệ điều hành sẽ tự lấy độ rộng màn hình chia cho `columnWidth` để tự động hiển thị 3 cột trên điện thoại nhỏ, và tự nhảy lên 6 hoặc 8 cột khi mở trên máy tính bảng.

### Câu 3: Thuộc tính `android:stretchMode` có ý nghĩa gì?
- **Trả lời:**
  - Quy định cách thức hệ thống chia đều phần không gian trống còn dư thừa giữa các cột sau khi đã vẽ xong kích thước chuẩn. Các chế độ bao gồm: `none`, `spacingWidth`, `columnWidth` (kéo rộng cột), `spacingWidthUniform`.
