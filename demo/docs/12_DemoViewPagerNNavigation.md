# Tài Liệu Hướng Dẫn & Vấn Đáp: 12_DemoViewPagerNNavigation

## 1. Tổng quan & Mục đích
- **Tên dự án:** `12_DemoViewPagerNNavigation`
- **Tài liệu gốc:** Chương 4.1 (`05Chuong04_1_Fragment.pdf` & `DemoViewerPaperNNavigation.pdf`) kết hợp cấu trúc dự án `ViewPagerNNavigation`.
- **Mục đích:** Xây dựng giao diện điều hướng đa màn hình kết hợp thanh điều hướng đáy `BottomNavigationView`, trình chuyển trang vuốt ngang `ViewPager`, thanh tiêu đề `ActionBar` và 3 `Fragment` con.
- **Thành phần chính:**
  - `ViewPager` (`@+id/view_pager`): Vuốt ngang qua lại giữa 3 trang.
  - `BottomNavigationView` (`@+id/bottom_navigation`): Thanh menu đáy gồm 3 mục `Person` (`@+id/person`), `Home` (`@+id/home`), `Settings` (`@+id/settings`), đổi màu icon/chữ trắng – vàng qua `res/color/color_navigation.xml`.
  - Các `Fragment`: `FirstFragment` ("Person"), `SecondFragment` ("Home"), `ThirdFragment` ("Setting").
  - `ViewPagerAdapter` kế thừa `FragmentStatePagerAdapter`: Quản lý danh sách và vòng đời của 3 Fragment trong `ViewPager`.
  - `ActionBar` (`getSupportActionBar().setTitle(...)`): Cập nhật tiêu đề trên thanh đầu màn hình đồng bộ theo từng trang.

---

## 2. Luồng hoạt động & Cơ chế Đồng bộ hai chiều (2-Way Sync)
```
[Người dùng vuốt ngang mViewPager]
              ↓
[ViewPager.OnPageChangeListener.onPageSelected(position)]
              ↓
[Đánh dấu tab tương ứng: mBottomNavigationView.getMenu().findItem(...).setChecked(true)]
[Cập nhật tiêu đề: getSupportActionBar().setTitle("Person" / "Home" / "Setting")]

                  ↕ (ĐỒNG BỘ 2 CHIỀU)

[Người dùng bấm chọn mục dưới mBottomNavigationView]
              ↓
[BottomNavigationView.OnNavigationItemSelectedListener.onNavigationItemSelected(item)]
              ↓
[Chuyển trang: mViewPager.setCurrentItem(0 / 1 / 2)]
[Cập nhật tiêu đề: getSupportActionBar().setTitle("Person" / "Home" / "Setting")]
```

---

## 3. Phân tích chi tiết Mã nguồn (Code Walkthrough)

### 3.1 Cấu hình Menu & Selector màu (`menu_navigation.xml` & `color_navigation.xml`)
- **`res/color/color_navigation.xml`:** Đổi màu icon và nhãn sang vàng (`@color/yellow`) khi tab đang được chọn (`state_checked="true"`) và trắng (`@color/white`) khi chưa chọn (`state_checked="false"`):
```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/white" android:state_checked="false" />
    <item android:color="@color/yellow" android:state_checked="true" />
</selector>
```
- **`res/menu/menu_navigation.xml`:** Định nghĩa 3 tab với 3 vector asset `person_white`, `home_white`, `settings_white`.

### 3.2 Khởi tạo Adapter cho ViewPager (`ViewPagerAdapter.java`)
```java
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
```

### 3.3 Xử lý đồng bộ trong `MainActivity.java`
```java
ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
        getSupportFragmentManager(),
        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
);
mViewPager.setAdapter(viewPagerAdapter);
mViewPager.setCurrentItem(0);

getSupportActionBar().setTitle("Person");

// 1. Khi người dùng vuốt mViewPager -> Cập nhật mBottomNavigationView + ActionBar
mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBottomNavigationView.getMenu().findItem(R.id.person).setChecked(true);
                getSupportActionBar().setTitle("Person");
                break;
            case 1:
                mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                getSupportActionBar().setTitle("Home");
                break;
            case 2:
                mBottomNavigationView.getMenu().findItem(R.id.settings).setChecked(true);
                getSupportActionBar().setTitle("Setting");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
});

// 2. Khi người dùng bấm mBottomNavigationView -> Chuyển mViewPager + ActionBar
mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.person) {
            mViewPager.setCurrentItem(0);
            getSupportActionBar().setTitle("Person");
        } else if (itemId == R.id.home) {
            mViewPager.setCurrentItem(1);
            getSupportActionBar().setTitle("Home");
        } else if (itemId == R.id.settings) {
            mViewPager.setCurrentItem(2);
            getSupportActionBar().setTitle("Setting");
        }
        return true;
    }
});
```

---

## 4. Bộ câu hỏi vấn đáp (Q&A) thường gặp của Giảng viên

### Câu 1: `Fragment` là gì? Vòng đời của `Fragment` gồm những hàm nào và khác gì `Activity`?
- **Trả lời:**
  - `Fragment` biểu diễn một hành vi hoặc một phần giao diện người dùng (giống như một *sub-activity*) có bố cục XML và vòng đời riêng, nhưng bắt buộc phải gắn vào một `Activity` chứa nó.
  - **Thứ tự các callback vòng đời của Fragment (theo slide Chương 4.1):**
    - Khi gắn vào Activity và chạy: `onAttach()` → `onCreate()` → `onCreateView()` → `onActivityCreated()` → `onStart()` → `onResume()`.
    - Khi tạm dừng / gỡ bỏ: `onPause()` → `onStop()` → `onDestroyView()` → `onDestroy()` → `onDetach()`.
  - **Khác biệt chính so với Activity:**
    - `Activity` dựng giao diện ngay trong `onCreate()` (`setContentView`), còn `Fragment` khởi tạo dữ liệu ở `onCreate()` và vẽ giao diện XML ở `onCreateView()` (trả về đối tượng `View`, hoặc `null` nếu là *Headless Fragment*).
    - Vòng đời `Fragment` phụ thuộc trực tiếp vào `Activity` chứa nó: khi Activity bị `onPause()` hay `onDestroy()` thì toàn bộ Fragment bên trong cũng tạm dừng hoặc bị hủy theo.

### Câu 2: Tại sao trong `themes.xml` phải dùng `Theme.Material3.DayNight` (có ActionBar) thay vì `.NoActionBar`?
- **Trả lời:**
  - Trong `MainActivity`, ta gọi `getSupportActionBar().setTitle(...)` để cập nhật tiêu đề `"Person"`, `"Home"`, `"Setting"` trên thanh tiêu đề.
  - Nếu theme kế thừa `.NoActionBar`, `getSupportActionBar()` sẽ trả về `null` và gây lỗi dừng ứng dụng (`NullPointerException`) ngay khi mở màn hình.

### Câu 3: Sự khác nhau giữa `FragmentPagerAdapter` và `FragmentStatePagerAdapter` là gì? Ý nghĩa của `BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT`?
- **Trả lời:**
  - `FragmentPagerAdapter`: Giữ nguyên đối tượng Fragment trong bộ nhớ sau khi tạo, chỉ hủy View (`onDestroyView`) khi trượt khuất; phù hợp cho số ít trang cố định.
  - `FragmentStatePagerAdapter`: Hủy toàn bộ Fragment khi trượt ra xa và chỉ lưu trạng thái (`savedInstanceState`), giúp tiết kiệm bộ nhớ RAM khi có nhiều trang.
  - `BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT`: Đảm bảo chỉ duy nhất Fragment đang hiển thị trước mắt người dùng mới đi vào trạng thái `onResume()`, còn các Fragment kế cận được nạp trước chỉ dừng ở `onStart()`.

### Câu 4: Tại sao trong `onNavigationItemSelected` dùng `if / else if` cho `R.id.*` thay vì `switch (item.getItemId())`?
- **Trả lời:**
  - Kể từ Android Gradle Plugin (AGP) 8.0+ / 9.x, các mã định danh tài nguyên `R.id.*` mặc định không còn từ khóa `final` (*non-final resource IDs*) để tăng tốc độ biên dịch tăng dần.
  - Trong ngôn ngữ Java, biểu thức `case` của `switch` bắt buộc phải là hằng số tại thời điểm biên dịch (`constant expression`), nên dùng `if / else if` giúp code biên dịch chuẩn trên Android Studio mới mà vẫn giữ nguyên logic xử lý của bài học.
