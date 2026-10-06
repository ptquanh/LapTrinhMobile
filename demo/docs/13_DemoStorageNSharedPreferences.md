# Tài Liệu Hướng Dẫn & Vấn Đáp: 13_DemoStorageNSharedPreferences

## 1. Tổng quan & Mục đích
- **Tên dự án:** `13_DemoStorageNSharedPreferences`
- **Mục đích:** So sánh và thực hành 2 kỹ thuật lưu trữ dữ liệu cục bộ cốt lõi trên thiết bị Android:
  1. `SharedPreferences`: Lưu trữ dữ liệu cấu hình có cấu trúc dạng cặp Khóa - Giá trị (`Key - Value`).
  2. `Internal Storage`: Lưu trữ tệp tin văn bản thuần túy trực tiếp vào phân vùng bộ nhớ trong của ứng dụng thông qua luồng byte I/O (`FileOutputStream`, `FileInputStream`).
- **Thành phần chính:**
  - Nhóm SharedPreferences: `edtPrefKey`, `edtPrefValue`, các nút Save/Load.
  - Nhóm Internal Storage: `edtFileName`, `edtFileContent`, các nút Ghi tệp/Đọc tệp.

---

## 2. Luồng hoạt động & Cơ chế Lưu trữ (Data Storage Flow)

### 2.1 SharedPreferences Flow
```
[Tạo Editor] → preferences.edit()
     ↓
[Đưa dữ liệu vào bộ đệm] → editor.putString(key, value)
     ↓
[Ghi xuống đĩa] → editor.apply() (Bất đồng bộ)
     ↓ (File XML được lưu tại: /data/data/<package_name>/shared_prefs/)
```

### 2.2 Internal Storage File I/O Flow
```
[Ghi file] → openFileOutput(fileName, Context.MODE_PRIVATE) → Ghi byte → Đóng luồng (close)
     ↓ (File lưu tại: /data/data/<package_name>/files/)
[Đọc file] → openFileInput(fileName) → BufferedReader đọc từng dòng → Nạp vào TextView
```

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Thao tác với SharedPreferences (`MainActivity.java`)
```java
private static final String PREF_NAME = "MyConfigPrefs";

// 1. Lưu dữ liệu
SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
SharedPreferences.Editor editor = preferences.edit();
editor.putString("USER_NAME", edtKey.getText().toString().trim());
editor.apply(); // Ghi bất đồng bộ xuống ổ đĩa ngầm

// 2. Đọc dữ liệu
String savedUser = preferences.getString("USER_NAME", "Giá trị mặc định nếu không có");
```

### 3.2 Thao tác với Internal Storage File (`MainActivity.java`)
```java
// 1. Ghi file nội bộ
public void saveFileInternal(String filename, String content) {
    try (FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE)) {
        fos.write(content.getBytes(StandardCharsets.UTF_8));
        Toast.makeText(this, "Đã ghi file thành công!", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// 2. Đọc file nội bộ
public String readFileInternal(String filename) {
    StringBuilder sb = new StringBuilder();
    try (FileInputStream fis = openFileInput(filename);
         BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return sb.toString();
}
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: So sánh sự khác nhau giữa `editor.commit()` và `editor.apply()` trong SharedPreferences?
- **Trả lời:**
  - `commit()`: Thực hiện ghi dữ liệu xuống đĩa **đồng bộ** (synchronous). Nó chặn (block) luồng UI chính cho đến khi hoàn tất việc ghi tệp, và trả về giá trị `boolean` biểu thị kết quả thành công hay thất bại. Nếu ghi file lớn trên Main Thread có thể gây giật màn hình (ANR).
  - `apply()`: Đưa dữ liệu vào bộ nhớ đệm ngay lập tức, sau đó thực hiện ghi xuống đĩa **bất đồng bộ** (asynchronous) ở một luồng nền phía dưới. Nó không trả về giá trị và không làm đứng UI, do đó Google khuyến cáo nên dùng `apply()`.

### Câu 2: Các tệp tin lưu bằng `SharedPreferences` và `openFileOutput(..., MODE_PRIVATE)` thực chất nằm ở đâu trên điện thoại? Ứng dụng khác có đọc trộm được không?
- **Trả lời:**
  - Chúng được lưu trong thư mục riêng tư của ứng dụng tại đường dẫn: `/data/data/<tên_package_ứng_dụng>/`.
    - SharedPreferences lưu ở: `/data/data/<package>/shared_prefs/<tên_file>.xml`.
    - Internal Storage lưu ở: `/data/data/<package>/files/<tên_file>`.
  - **Bảo mật:** Chế độ `MODE_PRIVATE` áp dụng cơ chế phân quyền người dùng của Linux (Linux User-ID Sandbox). Mỗi ứng dụng là một User độc lập, nên các ứng dụng khác hoàn toàn **không thể** đọc hoặc chỉnh sửa dữ liệu này (trừ khi điện thoại đã bị Root).

### Câu 3: Khi người dùng vào Cài đặt và bấm nút "Gỡ cài đặt" (Uninstall) ứng dụng, các file lưu trong Internal Storage và SharedPreferences có còn tồn tại không?
- **Trả lời:**
  - Toàn bộ thư mục `/data/data/<package>/` bao gồm SharedPreferences, Internal Storage và Database SQLite đều sẽ bị hệ điều hành Android **xóa sạch hoàn toàn** tự động.
