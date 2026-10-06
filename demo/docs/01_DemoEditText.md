# Tài Liệu Hướng Dẫn & Vấn Đáp: 01_DemoEditText

## 1. Tổng quan & Mục đích
- **Tên dự án:** `01_DemoEditText`
- **Mục đích:** Hướng dẫn kỹ thuật cơ bản về thu thập dữ liệu nhập vào từ người dùng qua `EditText`, bắt sự kiện người dùng tương tác qua `Button`, và cập nhật kết quả hiển thị ra `TextView`.
- **Thành phần chính:**
  - `EditText` (nhập văn bản).
  - `Button` (kích hoạt sự kiện submit/nhập).
  - `TextView` (hiển thị nội dung vừa nhập).

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. **Khởi tạo:**
   - Khi Activity khởi chạy, hàm `onCreate()` được gọi.
   - Gọi `setContentView(R.layout.activity_main)` để nạp giao diện XML vào bộ nhớ (inflate layout tree).
   - Ánh xạ các thành phần giao diện từ XML sang đối tượng Java bằng `findViewById()`.
2. **Lắng nghe sự kiện:**
   - Gán lắng nghe sự kiện bấm nút `btnSubmit.setOnClickListener(...)`.
3. **Xử lý sự kiện (Event Handling):**
   - Khi người dùng bấm nút:
     - Đọc nội dung từ `EditText`: `String input = edtName.getText().toString().trim();`.
     - Kiểm tra dữ liệu hợp lệ (nếu rỗng thì cảnh báo hoặc yêu cầu nhập).
     - Gán giá trị vào `TextView`: `tvResult.setText("Xin chào: " + input);`.
     - Ẩn bàn phím ảo (InputMethodManager) để tối ưu trải nghiệm người dùng (UX).

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Giao diện XML (`activity_main.xml`)
- Layout chính: `LinearLayout` (chiều dọc `android:orientation="vertical"`).
- Thuộc tính quan trọng:
  - `android:layout_width="match_parent"`: Chiếm toàn bộ chiều rộng màn hình cha.
  - `android:layout_height="wrap_content"`: Kích thước vừa khít với nội dung bên trong.
  - `android:hint="Nhập họ và tên..."`: Dòng chữ gợi ý mờ hiển thị khi ô nhập chưa có dữ liệu.
  - `android:inputType="textPersonName"`: Định nghĩa kiểu dữ liệu nhập để hệ điều hành hiển thị bàn phím ảo phù hợp (chữ hoa đầu dòng, có gợi ý từ,...).

### 3.2 Logic Java (`MainActivity.java`)
```java
public class MainActivity extends AppCompatActivity {
    private EditText edtName;
    private Button btnSubmit;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        edtName = findViewById(R.id.edtName);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvResult = findViewById(R.id.tvResult);

        // Lắng nghe sự kiện click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                if (name.isEmpty()) {
                    edtName.setError("Vui lòng không để trống!");
                    return;
                }
                tvResult.setText("Kết quả: " + name);
            }
        });
    }
}
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Hàm `onCreate(Bundle savedInstanceState)` có vai trò gì? Tham số `savedInstanceState` dùng để làm gì?
- **Trả lời:**
  - `onCreate()` là hàm đầu tiên được hệ điều hành gọi trong vòng đời của Activity khi nó được tạo ra. Nhiệm vụ chính là khởi tạo các biến thành viên, nạp giao diện thông qua `setContentView()`, và thiết lập các sự kiện lắng nghe (listeners).
  - `savedInstanceState` là một đối tượng `Bundle` chứa trạng thái đã lưu trước đó của Activity (ví dụ khi Activity bị hệ điều hành hủy tạm thời do xoay màn hình hoặc thiếu RAM). Nếu Activity được mở lần đầu tiên, `savedInstanceState` sẽ có giá trị `null`.

### Câu 2: Tại sao phải gọi `toString().trim()` sau hàm `edtName.getText()`?
- **Trả lời:**
  - `edtName.getText()` không trả về trực tiếp kiểu `java.lang.String` mà trả về đối tượng `Editable` (cho phép chỉnh sửa văn bản trong bộ nhớ đệm). Do đó, cần gọi `.toString()` để chuyển sang chuỗi văn bản thuần túy.
  - Hàm `.trim()` dùng để loại bỏ các khoảng trắng thừa ở đầu và cuối chuỗi, tránh tình trạng người dùng chỉ gõ phím cách mà hệ thống vẫn coi là có dữ liệu.

### Câu 3: Thuộc tính `android:inputType` trên `EditText` có tác dụng gì? Nếu muốn nhập mật khẩu thì dùng thuộc tính nào?
- **Trả lời:**
  - `android:inputType` quyết định kiểu dữ liệu mà người dùng được phép nhập và chỉ định loại bàn phím mềm hiển thị (ví dụ: `text`, `number`, `phone`, `textEmailAddress`).
  - Nếu muốn nhập mật khẩu thì dùng: `android:inputType="textPassword"` (dấu ký tự ẩn dạng chấm tròn hoặc sao).

### Câu 4: Sự khác nhau giữa `match_parent` và `wrap_content`?
- **Trả lời:**
  - `match_parent` (trước đây là `fill_parent`): View sẽ mở rộng kích thước để chiếm trọn vẹn không gian khả dụng của View cha chứa nó.
  - `wrap_content`: View chỉ chiếm kích thước tối thiểu vừa đủ để bao bọc nội dung (content) hiển thị bên trong nó.
