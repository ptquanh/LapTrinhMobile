# Tài Liệu Hướng Dẫn & Vấn Đáp: 11_DemoGridViewNangCao

## 1. Tổng quan & Mục đích
- **Tên dự án:** `11_DemoGridViewNangCao`
- **Mục đích:** Xây dựng ứng dụng thư viện ảnh đa phương tiện (Gallery Grid) chuyên nghiệp, hiển thị lưới hình ảnh kèm tiêu đề, áp dụng Custom Adapter, ViewHolder và truyền dữ liệu qua Intent sang màn hình chi tiết (`PictureActivity`).
- **Thành phần chính:**
  - Model `HinhAnh`: Chứa tên ảnh và ID tài nguyên ảnh (`resId`).
  - Giao diện ô lưới: `item_grid.xml`.
  - Custom Adapter: `HinhAnhAdapter` kế thừa `BaseAdapter`.
  - Màn hình chi tiết: `PictureActivity` nhận Intent và hiển thị ảnh toàn màn hình.

---

## 2. Luồng hoạt động & Truyền dữ liệu giữa các Activity
```
[MainActivity] (Hiển thị GridView các bức ảnh)
       ↓ Người dùng bấm chọn ảnh tại vị trí `position`
[HinhAnh item = list.get(position)]
       ↓ Tạo Intent tường minh (Explicit Intent)
[Intent intent = new Intent(MainActivity.this, PictureActivity.class)]
       ↓ Đóng gói dữ liệu vào Bundle/Extra: intent.putExtra("hinhanh", item)
[startActivity(intent)]
       ↓ Chuyển màn hình
[PictureActivity] (Nhận Intent qua getIntent(), đọc dữ liệu và set ảnh toàn màn hình)
```

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Model triển khai Serializable (`HinhAnh.java`)
```java
public class HinhAnh implements Serializable {
    private String ten;
    private int hinh;

    public HinhAnh(String ten, int hinh) {
        this.ten = ten;
        this.hinh = hinh;
    }
    public String getTen() { return ten; }
    public int getHinh() { return hinh; }
}
```
> **Lưu ý:** Bắt buộc `implements Serializable` (hoặc `Parcelable`) để có thể truyền đối tượng qua `Intent.putExtra()`.

### 3.2 Bắt sự kiện chuyển màn hình (`MainActivity.java`)
```java
gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
        intent.putExtra("dataHinhAnh", arrayHinhAnh.get(position));
        startActivity(intent);
    }
});
```

### 3.3 Nhận dữ liệu tại màn hình đích (`PictureActivity.java`)
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_picture);

    ImageView imgDetail = findViewById(R.id.imgDetail);
    TextView tvDetail = findViewById(R.id.tvDetail);

    Intent intent = getIntent();
    HinhAnh hinhAnh = (HinhAnh) intent.getSerializableExtra("dataHinhAnh");

    if (hinhAnh != null) {
        tvDetail.setText(hinhAnh.getTen());
        imgDetail.setImageResource(hinhAnh.getHinh());
    }
}
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Tại sao lớp `HinhAnh` phải `implements Serializable` khi truyền qua Intent?
- **Trả lời:**
  - `Intent` trong Android hoạt động dựa trên cơ chế IPC (Inter-Process Communication) của nhân Linux. Nó chỉ hỗ trợ gửi các kiểu dữ liệu nguyên thủy (`int`, `boolean`, `String`).
  - Để gửi một đối tượng Java tự định nghĩa, đối tượng đó phải được tuần tự hóa (serialize) thành một chuỗi byte nhị phân. `implements Serializable` là đánh dấu (marker interface) báo cho máy ảo Java biết lớp này được phép chuyển đổi trạng thái thành byte stream để truyền đi và tái tạo lại ở Activity nhận.

### Câu 2: Giữa `Serializable` và `Parcelable`, cái nào có tốc độ thực thi nhanh hơn trong Android? Tại sao?
- **Trả lời:**
  - `Parcelable` nhanh hơn rất nhiều (từ 2 đến 10 lần) so với `Serializable`.
  - **Lý do:** `Serializable` là interface chuẩn của Java, dùng cơ chế Reflection lúc runtime để phân tích đối tượng nên tốn bộ nhớ và chậm. Trong khi đó, `Parcelable` được thiết kế riêng cho Android SDK, yêu cầu lập trình viên tự ghi và đọc rõ ràng từng trường vào `Parcel`, không dùng Reflection nên tốc độ đọc/ghi cực nhanh.

### Câu 3: Làm thế nào để thêm nút quay lại (Back) trên thanh ActionBar của `PictureActivity`?
- **Trả lời:**
  - Trong `onCreate()` của `PictureActivity`, gọi:
    ```java
    if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    ```
  - Và ghi đè phương thức:
    ```java
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    ```
