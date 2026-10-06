# Tài Liệu Hướng Dẫn & Vấn Đáp: 07_DemoImageView

## 1. Tổng quan & Mục đích
- **Tên dự án:** `07_DemoImageView`
- **Mục đích:** Nắm vững cách hiển thị hình ảnh (`ImageView`), hiểu rõ các chế độ căn chỉnh tỷ lệ (`scaleType`), và cách thay đổi ảnh động bằng code Java.
- **Thành phần chính:**
  - `ImageView`: View hiển thị ảnh từ thư mục `res/drawable` hoặc Bitmap trong bộ nhớ.
  - Các chế độ hiển thị: `fitCenter`, `centerCrop`, `centerInside`, `fitXY`.

---

## 2. Luồng hoạt động (Architecture & Event Flow)
1. Định nghĩa `ImageView` trong XML và liên kết với tài nguyên qua `android:src="@drawable/image_name"`.
2. Trong Java, ánh xạ qua `findViewById()`.
3. Khi cần đổi ảnh theo hành động người dùng:
   - Gọi `imageView.setImageResource(R.drawable.ten_anh_moi)`.
   - Thay đổi chế độ co giãn: `imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)`.

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Bảng so sánh các thuộc tính `android:scaleType`
| scaleType | Mô tả hoạt động | Tỷ lệ ảnh (Aspect Ratio) |
|---|---|---|
| `center` | Giữ nguyên kích thước gốc của ảnh và đặt giữa khung hình, ảnh lớn sẽ bị cắt viền. | Giữ nguyên |
| `centerCrop` | Co giãn ảnh sao cho cả chiều dài và rộng đều lấp đầy khung hình, phần thừa bị cắt xén. | Giữ nguyên |
| `fitCenter` (Mặc định) | Co giãn ảnh sao cho toàn bộ ảnh nằm trọn trong khung hình, có thể thừa viền trống. | Giữ nguyên |
| `fitXY` | Co giãn ảnh lấp đầy toàn bộ chiều rộng và cao của khung hình, ảnh bị méo nếu sai tỷ lệ. | Bị biến dạng (méo ảnh) |

### 3.2 Logic Java (`MainActivity.java`)
```java
ImageView imgDisplay = findViewById(R.id.imgDisplay);
Button btnChangeImage = findViewById(R.id.btnChangeImage);

btnChangeImage.setOnClickListener(v -> {
    imgDisplay.setImageResource(R.drawable.sample_photo);
    imgDisplay.setScaleType(ImageView.ScaleType.CENTER_CROP);
});
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: Khi hiển thị một tấm ảnh có dung lượng lớn (ví dụ chụp từ máy ảnh 12MB) vào `ImageView`, vấn đề nghiêm trọng gì thường xảy ra? Giải pháp là gì?
- **Trả lời:**
  - **Vấn đề:** Dễ gây ra lỗi `OutOfMemoryError` (OOM) làm ứng dụng bị văng đột ngột vì bộ nhớ heap dành cho mỗi ứng dụng Android bị giới hạn (thường từ 128MB - 256MB).
  - **Giải pháp:** Phải giải nén thu nhỏ ảnh (downsampling) bằng `BitmapFactory.Options` với thuộc tính `inSampleSize`, hoặc sử dụng các thư viện quản lý ảnh chuyên nghiệp của bên thứ ba như **Glide** hoặc **Picasso** để tự động nén, quản lý bộ nhớ đệm (caching) và tái sử dụng bộ nhớ.

### Câu 2: Trong làm ứng dụng thực tế (ví dụ ảnh đại diện Avatar tròn), em nên dùng `scaleType` nào?
- **Trả lời:**
  - Nên dùng `centerCrop`. Vì nó đảm bảo ảnh phủ kín toàn bộ khung hình vuông/tròn của avatar mà không làm méo mặt người dùng (giữ nguyên aspect ratio).

### Câu 3: Thuộc tính `app:srcCompat` khác gì với `android:src`?
- **Trả lời:**
  - `android:src` là thuộc tính thuần của Android SDK, trên các phiên bản Android cũ (dưới API 21) không hỗ trợ định dạng ảnh vector drawable (`.xml`).
  - `app:srcCompat` là thuộc tính thuộc thư viện `AndroidX / AppCompat`, hỗ trợ vẽ tương thích ngược tất cả các Vector Drawable trên mọi phiên bản Android cũ mà không bị crash.
