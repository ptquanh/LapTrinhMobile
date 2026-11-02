# Kế Hoạch Triển Khai Đề Kiểm Tra Giữa Kỳ LTTBDD

## Goal
Triển khai toàn diện đồ án Android Java theo cấu trúc và yêu cầu của đề kiểm tra giữa kỳ (GridView, ListView giỏ hàng kèm Checkbox, 3 Fragment, ViewPager không dùng ViewPager2 đồng bộ 2 chiều với BottomNavigationView, Tab Profile).

## Tasks
- [x] Task 1: Thiết kế Model `Book.java` và bộ vector drawables cho bìa sách, icon navigation, avatar → Verify: Các file tài nguyên xml và class Book đầy đủ thuộc tính
- [x] Task 2: Tạo menu BottomNavigation và layout giao diện cho MainActivity, 3 Fragment và các item layout (item_book_grid, item_cart_list) → Verify: Layout compile không lỗi XML
- [x] Task 3: Triển khai các Adapter (`ViewPagerAdapter`, `BookGridAdapter`, `CartListAdapter`) đúng theo đề bài → Verify: Kế thừa đúng `FragmentPagerAdapter` và `BaseAdapter`
- [x] Task 4: Triển khai 3 Fragment (`BookGridFragment`, `CartFragment`, `ProfileFragment`) → Verify: Hiển thị đúng dữ liệu danh sách sách và giỏ hàng, profile
- [x] Task 5: Triển khai `MainActivity.java` kết nối ViewPager + BottomNavigationView, thiết lập đồng bộ 2 chiều → Verify: Vuốt tab đổi icon bottom nav, bấm icon bottom nav chuyển tab viewpager
- [x] Task 6: Biên dịch ứng dụng bằng `./gradlew assembleDebug` và kiểm tra toàn diện → Verify: Build SUCCESSFUL, sinh file `app-debug.apk` thành công

## Done When
- [x] Đáp ứng đầy đủ 100% 6 tiêu chí chấm điểm trong đề bài
- [x] Build Gradle thành công không lỗi
