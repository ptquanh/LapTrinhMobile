# Tài Liệu Hướng Dẫn & Vấn Đáp: 05_DemoToggleButton

## 1. Tổng quan & Mục đích
- **Tên dự án:** `05_DemoToggleButton`
- **Mục đích:** Tìm hiểu cách điều khiển trạng thái bật/tắt (ON/OFF - nhị phân) bằng các thành phần `ToggleButton` và `Switch`.
- **Thành phần chính:**
  - `ToggleButton`: Nút chuyển đổi trạng thái với 2 nhãn văn bản (ví dụ "BẬT" và "TẮT").
  - `Switch`: Thanh trượt bật tắt chuẩn Material Design.
  - `TextView`: Hiển thị trạng thái hiện tại.

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. Người dùng chạm vào `ToggleButton` hoặc gạt `Switch`.
2. Trạng thái boolean (`isChecked`) nội tại của widget thay đổi (`true` hoặc `false`).
3. Widget kích hoạt sự kiện lắng nghe `CompoundButton.OnCheckedChangeListener`.
4. Phương thức `onCheckedChanged(CompoundButton buttonView, boolean isChecked)` nhận giá trị mới và cập nhật giao diện hoặc lưu cấu hình.

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Giao diện XML (`activity_main.xml`)
```xml
<ToggleButton
    android:id="@+id/toggleButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textOn="ĐANG BẬT"
    android:textOff="ĐÃ TẮT"
    android:checked="false" />

<androidx.appcompat.widget.SwitchCompat
    android:id="@+id/switchWifi"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Kết nối WiFi" />
```

### 3.2 Logic Java (`MainActivity.java`)
```java
toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            tvStatus.setText("Trạng thái: BẬT");
        } else {
            tvStatus.setText("Trạng thái: TẮT");
        }
    }
});
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: `ToggleButton`, `Switch`, `CheckBox`, và `RadioButton` đều kế thừa từ lớp cha nào trong Android SDK?
- **Trả lời:**
  - Tất cả các view này đều kế thừa từ lớp trừu tượng `android.widget.CompoundButton` (và `CompoundButton` kế thừa từ `Button` -> `TextView` -> `View`).
  - Do đó, tất cả chúng đều dùng chung interface lắng nghe là `CompoundButton.OnCheckedChangeListener`.

### Câu 2: Làm sao để kiểm tra trạng thái hiện tại của `ToggleButton` bằng code bất kỳ lúc nào mà không cần đợi sự kiện click?
- **Trả lời:**
  - Sử dụng phương thức getter: `boolean state = toggleButton.isChecked();`.

### Câu 3: Thuộc tính `android:textOn` và `android:textOff` có tác dụng gì?
- **Trả lời:**
  - `android:textOn`: Định nghĩa chuỗi văn bản hiển thị khi nút đang ở trạng thái kích hoạt (`checked == true`).
  - `android:textOff`: Định nghĩa chuỗi văn bản hiển thị khi nút ở trạng thái tắt (`checked == false`).
