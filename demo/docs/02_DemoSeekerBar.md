# Tài Liệu Hướng Dẫn & Vấn Đáp: 02_DemoSeekerBar

## 1. Tổng quan & Mục đích
- **Tên dự án:** `02_DemoSeekerBar`
- **Mục đích:** Tìm hiểu cách sử dụng thành phần điều khiển thanh trượt `SeekBar` trong Android để chọn giá trị liên tục trong một khoảng xác định (ví dụ: âm lượng, độ sáng, tiến trình phát nhạc).
- **Thành phần chính:**
  - `SeekBar`: Cho phép người dùng kéo thả con trượt hoặc chạm để chọn giá trị số.
  - `TextView`: Hiển thị giá trị hiện tại theo thời gian thực (realtime) khi người dùng thao tác.

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. **Khởi tạo:**
   - Trong `onCreate()`, ánh xạ `SeekBar` và `TextView`.
   - Thiết lập giá trị cực đại `seekBar.setMax(100)` và giá trị ban đầu `seekBar.setProgress(50)`.
2. **Đăng ký Listener:**
   - Sử dụng `seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {...})`.
3. **Các trạng thái của Listener:**
   - `onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)`: Kích hoạt liên tục mỗi khi giá trị con trượt thay đổi.
   - `onStartTrackingTouch(SeekBar seekBar)`: Kích hoạt đúng thời điểm người dùng bắt đầu chạm ngón tay vào con trượt.
   - `onStopTrackingTouch(SeekBar seekBar)`: Kích hoạt khi người dùng nhấc ngón tay ra khỏi màn hình (kết thúc thao tác kéo).

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Giao diện XML (`activity_main.xml`)
```xml
<SeekBar
    android:id="@+id/seekBar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:max="100"
    android:progress="20" />
```

### 3.2 Logic Java (`MainActivity.java`)
```java
seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // progress: giá trị hiện tại (0 -> max)
        // fromUser: true nếu do người dùng kéo ngón tay, false nếu do code setProgress()
        tvProgress.setText("Tiến độ: " + progress + " / " + seekBar.getMax());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Thường dùng để tạm dừng phát nhạc hoặc chuẩn bị bộ đệm
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Thường dùng để seek nhạc tới vị trí mới hoặc lưu cấu hình vào SharedPreferences
    }
});
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Tham số `boolean fromUser` trong hàm `onProgressChanged` dùng để phân biệt điều gì? Khi nào nó có giá trị `false`?
- **Trả lời:**
  - `fromUser` dùng để xác định sự thay đổi tiến trình xuất phát từ thao tác vật lý của người dùng (`true`) hay từ hệ thống/mã lập trình (`false`).
  - Giá trị là `false` khi lập trình viên chủ động gọi lệnh `seekBar.setProgress(value)` bằng code (ví dụ: tiến trình bài hát tự động tăng theo timer của MediaPlayer).

### Câu 2: Trong ứng dụng nghe nhạc, em sẽ đặt lệnh `mediaPlayer.seekTo(progress)` ở hàm callback nào của SeekBar? Tại sao?
- **Trả lời:**
  - Nên đặt ở hàm `onStopTrackingTouch(SeekBar seekBar)`.
  - **Lý do:** Hàm `onProgressChanged()` bị gọi liên tục hàng chục lần mỗi giây khi người dùng lướt ngón tay. Nếu gọi `seekTo()` liên tục trong `onProgressChanged()`, âm thanh sẽ bị giật cục và gây nghẽn hiệu năng I/O của MediaPlayer. Chỉ khi người dùng nhấc ngón tay ra (`onStopTrackingTouch`), ta mới thực hiện seek 1 lần duy nhất đến mốc thời gian đích.

### Câu 3: Làm thế nào để thay đổi màu sắc con trượt (thumb) và thanh tiến trình (track) của SeekBar?
- **Trả lời:**
  - Sử dụng thuộc tính `android:thumb` để thay đổi hình dạng/màu con trượt.
  - Sử dụng `android:progressDrawable` để chỉ định drawable layer-list tùy biến màu sắc thanh nền (background) và thanh tiến trình (progress).
