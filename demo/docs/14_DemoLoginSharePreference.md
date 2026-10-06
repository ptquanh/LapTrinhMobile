# Tài Liệu Hướng Dẫn & Vấn Đáp: 14_DemoLoginSharePreference

## 1. Tổng quan & Mục đích
- **Tên dự án:** `14_DemoLoginSharePreference`
- **Mục đích:** Xây dựng tính năng "Ghi nhớ đăng nhập" (Remember Me / Auto-login) thực tế bằng cách lưu trữ trạng thái phiên đăng nhập và tài khoản người dùng vào `SharedPreferences`.
- **Thành phần chính:**
  - `MainActivity`: Màn hình Đăng nhập (Username, Password, CheckBox "Ghi nhớ mật khẩu", Button Đăng nhập).
  - `MainActivity2`: Màn hình Trang chủ (Dashboard) sau khi đăng nhập thành công kèm nút "Đăng xuất" (Logout).
  - `SharedPreferences`: Lưu cờ `IS_LOGGED_IN`, `USERNAME`, `PASSWORD`.

---

## 2. Luồng hoạt động (Operational Flow & Login State Machine)
```
[Khởi chạy ứng dụng (MainActivity)]
                 ↓
[Đọc SharedPreferences: boolean isRemember = pref.getBoolean("REMEMBER", false)]
                 ↓
    ┌────────────┴────────────┐
    ↓ (Nếu isRemember == true) ↓ (Nếu false)
[Tự điền Username & Password]  [Để trống các ô nhập]
    ↓ (Nếu IS_LOGGED_IN == true)
[Tự động Intent chuyển sang MainActivity2 (Auto Login)]

-----------------------------------------------------------
[Người dùng bấm nút Đăng Xuất tại MainActivity2]
                 ↓
[Xóa hoặc đặt lại IS_LOGGED_IN = false]
                 ↓
[finish() và quay lại MainActivity]
```

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Kiểm tra trạng thái đã lưu khi mở màn hình (`MainActivity.java`)
```java
private static final String PREF_NAME = "LOGIN_PREFS";
private SharedPreferences sharedPreferences;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

    // Kiểm tra nếu người dùng đã tích chọn "Ghi nhớ" từ lần trước
    boolean isRemembered = sharedPreferences.getBoolean("REMEMBER", false);
    if (isRemembered) {
        edtUser.setText(sharedPreferences.getString("USERNAME", ""));
        edtPass.setText(sharedPreferences.getString("PASSWORD", ""));
        cbRemember.setChecked(true);
    }

    btnLogin.setOnClickListener(v -> handleLogin());
}

private void handleLogin() {
    String user = edtUser.getText().toString().trim();
    String pass = edtPass.getText().toString().trim();

    // Giả lập kiểm tra tài khoản hợp lệ
    if (user.equals("admin") && pass.equals("123456")) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (cbRemember.isChecked()) {
            editor.putString("USERNAME", user);
            editor.putString("PASSWORD", pass);
            editor.putBoolean("REMEMBER", true);
        } else {
            editor.clear(); // Xóa sạch dữ liệu nếu bỏ tích chọn ghi nhớ
        }
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.apply();

        // Chuyển sang màn hình Dashboard
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
        finish(); // Đóng màn hình đăng nhập để khi bấm phím Back không quay lại đây
    } else {
        Toast.makeText(this, "Sai thông tin tài khoản!", Toast.LENGTH_SHORT).show();
    }
}
```

### 3.2 Xử lý Đăng xuất tại màn hình đích (`MainActivity2.java`)
```java
btnLogout.setOnClickListener(v -> {
    SharedPreferences pref = getSharedPreferences("LOGIN_PREFS", MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putBoolean("IS_LOGGED_IN", false);
    // Nếu muốn xóa luôn mật khẩu đã ghi nhớ: editor.clear();
    editor.apply();

    // Quay lại màn hình đăng nhập
    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
    startActivity(intent);
    finish();
});
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Tại sao sau khi gọi `startActivity(intent)` chuyển sang màn hình chính lại nên gọi thêm hàm `finish()`?
- **Trả lời:**
  - Hàm `finish()` dùng để kết thúc và đóng hoàn toàn `MainActivity` (màn hình Đăng nhập) ra khỏi ngăn xếp chuyển hướng (Back Stack).
  - **Mục đích:** Khi người dùng đã đăng nhập thành công vào `MainActivity2`, nếu họ nhấn phím Back vật lý trên điện thoại, ứng dụng sẽ thoát ra màn hình Home của điện thoại, thay vì bị lùi ngược trở lại form đăng nhập (điều này vi phạm logic trải nghiệm người dùng).

### Câu 2: Trong các ứng dụng sản phẩm thực tế, có nên lưu mật khẩu dạng chuỗi văn bản thuần (plain-text) vào `SharedPreferences` không? Vì sao?
- **Trả lời:**
  - **Không nên.** Dù SharedPreferences nằm trong vùng nhớ `MODE_PRIVATE`, nhưng nếu thiết bị người dùng bị Root hoặc cài phần mềm độc hại có quyền root, file XML này vẫn có thể bị mở ra và đọc được mật khẩu dạng rõ.
  - **Giải pháp thực tế:** 
    1. Không lưu mật khẩu, chỉ lưu **Access Token** hoặc **Refresh Token** do máy chủ (Backend) cấp sau khi đăng nhập thành công.
    2. Nếu bắt buộc phải lưu trữ thông tin nhạy cảm ở client, sử dụng thư viện **EncryptedSharedPreferences** của Google (nằm trong bộ AndroidX Security), thư viện này tự động mã hóa dữ liệu 2 chiều chuẩn AES-256 trước khi ghi xuống file.

### Câu 3: Làm thế nào để tự động chuyển thẳng vào màn hình chính (`MainActivity2`) nếu người dùng đã đăng nhập từ trước đó mà không cần hiển thị lại form đăng nhập?
- **Trả lời:**
  - Trong hàm `onCreate()` của `MainActivity`, trước khi gọi `setContentView()`, ta kiểm tra cờ `sharedPreferences.getBoolean("IS_LOGGED_IN", false)`.
  - Nếu bằng `true`, ta tạo ngay `Intent` chuyển sang `MainActivity2`, gọi `startActivity()`, sau đó gọi `finish()` và `return;` ngay lập tức để không nạp giao diện đăng nhập nữa.
