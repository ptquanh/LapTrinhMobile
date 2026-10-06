# CẨM NANG ÔN TẬP VÀ VẤN ĐÁP THỰC HÀNH LẬP TRÌNH THIẾT BỊ DI ĐỘNG (ANDROID)

Bộ tài liệu tổng hợp chi tiết toàn bộ **14 bài thực hành Demo**, giải thích kiến trúc, luồng hoạt động, phân tích từng đoạn mã nguồn và bộ câu hỏi vấn đáp thường gặp giúp sinh viên tự tin trả lời thuyết phục trước Hội đồng Giảng viên.

---

## 📑 Danh Mục Toàn Bộ 14 Demo

| STT | Dự án Demo | Chủ đề trọng tâm | File tài liệu chi tiết |
|:---:|---|---|---|
| **01** | `01_DemoEditText` | Tương tác nhập xuất, Button, EditText, TextView, kiểm tra hợp lệ | [Xem tài liệu](01_DemoEditText.md) |
| **02** | `02_DemoSeekerBar` | Thanh trượt SeekBar, lắng nghe sự kiện OnSeekBarChangeListener | [Xem tài liệu](02_DemoSeekerBar.md) |
| **03** | `03_DemoImageButton` | Nút bấm hình ảnh, phân biệt ImageButton vs Button, trợ năng TalkBack | [Xem tài liệu](03_DemoImageButton.md) |
| **04** | `04_DemoToast` | Thông báo tạm thời, Toast chuẩn, Custom Toast, so sánh với Snackbar | [Xem tài liệu](04_DemoToast.md) |
| **05** | `05_DemoToggleButton` | Công tắc nhị phân ON/OFF, Switch, CompoundButton, OnCheckedChangeListener | [Xem tài liệu](05_DemoToggleButton.md) |
| **06** | `06_DemoWebView` | Nhúng trình duyệt web, WebSettings, JavaScript, xử lý WebViewClient & Back | [Xem tài liệu](06_DemoWebView.md) |
| **07** | `07_DemoImageView` | Hiển thị hình ảnh, các chế độ scaleType, bộ nhớ Heap và tránh lỗi OOM | [Xem tài liệu](07_DemoImageView.md) |
| **08** | `08_DemoListViewCoBan` | Danh sách cuộn 1 chiều, cơ chế Adapter, ArrayAdapter, cập nhật danh sách | [Xem tài liệu](08_DemoListViewCoBan.md) |
| **09** | `09_DemoListViewNangCao` | Custom ListView, Model OOP, tối ưu hiệu năng với ViewHolder Pattern | [Xem tài liệu](09_DemoListViewNangCao.md) |
| **10** | `10_DemoGridViewCoBan` | Lưới hiển thị 2 chiều GridView, cấu hình numColumns, khoảng cách lề | [Xem tài liệu](10_DemoGridViewCoBan.md) |
| **11** | `11_DemoGridViewNangCao` | Custom GridView Gallery ảnh, truyền đối tượng Serializable qua Intent | [Xem tài liệu](11_DemoGridViewNangCao.md) |
| **12** | `12_DemoViewPagerNNavigation` | Điều hướng BottomNavigationView kết hợp vuốt ngang ViewPager và Fragment | [Xem tài liệu](12_DemoViewPagerNNavigation.md) |
| **13** | `13_DemoStorageNSharedPreferences` | Lưu trữ cấu hình SharedPreferences vs File nội bộ Internal Storage | [Xem tài liệu](13_DemoStorageNSharedPreferences.md) |
| **14** | `14_DemoLoginSharePreference` | Tính năng ghi nhớ tài khoản (Remember Me), kiểm soát phiên đăng nhập | [Xem tài liệu](14_DemoLoginSharePreference.md) |

---

## 🎯 5 Kiến Thức Trọng Tâm "Bất Biến" Thường Bị Hỏi Nhiều Nhất

### 1. Vòng đời của Activity (Activity Lifecycle)
- **7 hàm callback:** `onCreate()` → `onStart()` → `onResume()` → `onPause()` → `onStop()` → `onDestroy()` → `onRestart()`.
- **Câu hỏi thầy hay hỏi:** *"Khi đang dùng app mà có cuộc gọi đến thì Activity vào hàm nào?"*
  - **Trả lời:** Activity rơi vào `onPause()` (nếu popup cuộc gọi che 1 phần) hoặc `onStop()` (nếu màn hình cuộc gọi che toàn bộ).
- **Câu hỏi:** *"Khi xoay ngang màn hình thì chuyện gì xảy ra?"*
  - **Trả lời:** Hệ điều hành hủy Activity cũ (`onPause` -> `onStop` -> `onDestroy`) và tạo mới lại hoàn toàn Activity mới (`onCreate` -> `onStart` -> `onResume`) để nạp lại layout phù hợp với hướng màn hình ngang.

### 2. Sự khác biệt giữa `dp`, `sp`, và `px`
- **`px` (Pixel vật lý):** Điểm ảnh thực tế của màn hình. Không nên dùng vì màn hình có mật độ điểm ảnh (dpi) khác nhau sẽ làm giao diện bị co rúm trên màn hình sắc nét và phóng to trên màn hình độ phân giải thấp.
- **`dp` / `dip` (Density-independent Pixels):** Đơn vị độc lập với mật độ điểm ảnh. Chuẩn $1dp = 1px$ trên màn hình 160 dpi. Dùng cho kích thước view, padding, margin.
- **`sp` (Scale-independent Pixels):** Tương tự `dp` nhưng co giãn tự động theo cỡ chữ mà người dùng cài đặt trong phần Cài đặt trợ năng của điện thoại. Dành riêng cho `textSize`.

### 3. Cơ chế View Recycling & ViewHolder Pattern trong Adapter
- Cực kỳ quan trọng ở các bài ListView và GridView nâng cao.
- **Tại sao cần convertView?** Để không phải tạo lại View mới khi cuộn danh sách, tái sử dụng các View đã cuộn khuất màn hình.
- **Tại sao cần ViewHolder?** Để không phải gọi `findViewById()` nhiều lần, tránh việc duyệt cây view liên tục gây giảm FPS và giật lag.

### 4. SharedPreferences: `commit()` vs `apply()`
- `commit()`: Đồng bộ (blocking Main Thread), có trả về boolean.
- `apply()`: Bất đồng bộ (ghi ở background thread), không block UI, hiệu năng cao hơn.

### 5. Cơ chế Intent trong Android
- **Explicit Intent (Tường minh):** Chỉ rõ tên lớp Activity đích cần mở (ví dụ: `new Intent(this, MainActivity2.class)`).
- **Implicit Intent (Không tường minh):** Không chỉ rõ lớp cụ thể mà chỉ ra hành động cần làm (ví dụ: `ACTION_VIEW` mở đường link web, `ACTION_DIAL` mở bàn phím gọi điện), hệ điều hành sẽ tìm ứng dụng có bộ lọc Intent Filter phù hợp để mở.
