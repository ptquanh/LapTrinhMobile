# 15_DemoWeather Plan

## Goal
Xây dựng hoàn chỉnh dự án Android `demo/15_DemoWeather` (tra cứu thời tiết theo giờ qua WeatherAPI bằng Volley + Picasso, kết hợp `NetworkChangeReceiver` theo dõi trạng thái mạng và dữ liệu dự phòng) kèm tài liệu ôn tập vấn đáp `demo/docs/15_DemoWeather.md`.

## Tasks
- [x] Task 1: Khởi tạo cấu trúc Gradle dự án `demo/15_DemoWeather` (`settings.gradle`, `build.gradle`, `libs.versions.toml`, `gradle-wrapper`, `local.properties`) với thư viện `volley:1.2.1` và `picasso:2.8` → Verify: Cấu trúc khớp chuẩn các project trong `demo/`.
- [x] Task 2: Cấu hình `AndroidManifest.xml` cấp quyền `INTERNET`, `ACCESS_NETWORK_STATE` và thiết lập tài nguyên `colors.xml`, `strings.xml`, `themes.xml`, icon tìm kiếm `ic_baseline_search_24.xml` & icon thời tiết mặc định → Verify: Không dùng tím/violet, màu sắc sky-blue Material 3 rõ nét.
- [x] Task 3: Thiết kế `activity_main.xml` và `layout_weather_items.xml` giữ đúng toàn bộ ID chuẩn tài liệu PDF (`txtCityName`, `editTextTextPersonName`, `imgSearch`, `txtTemperature`, `imgSeasonForecast`, `txtCityTemperature`, `lvWeatherForecast`, `txtTimeForecast`, `imgForecast`, `txtTemperatureForecast`, `txtWindForecast`) kèm thanh trạng thái mạng & loading mượt mà, chuẩn `dp`/`sp` và vùng chạm >= `48dp` → Verify: Đầy đủ View ID và accessibility `contentDescription`.
- [x] Task 4: Cài đặt `WeatherForecastModel`, `WeatherAdapter` (áp dụng kiểm tra tái sử dụng `if (view == null)` chuẩn bài giảng + Picasso load icon `https:`), và `NetworkChangeReceiver` (kiểm tra WiFi / Mobile Data, tương thích Android 13+ `RECEIVER_NOT_EXPORTED`) → Verify: Đúng phong cách giảng dạy trên lớp, xử lý đúng vòng đời `onStart`/`onStop`.
- [x] Task 5: Cài đặt `MainActivity` kết nối Volley `JsonObjectRequest`, chuẩn hóa tên thành phố (`Uri.encode`), hỗ trợ nhấn Enter/Search trên bàn phím, tự động tải lại khi mạng khôi phục, và fallback dữ liệu mẫu khi lỗi API/mất mạng → Verify: Luồng tra cứu chạy tốt cả online lẫn offline/fallback.
- [x] Task 6: Viết tài liệu hướng dẫn & vấn đáp `demo/docs/15_DemoWeather.md` và cập nhật danh mục `demo/docs/README.md` → Verify: Đầy đủ sơ đồ luồng, phân tích code và câu hỏi vấn đáp thực tế.
- [x] Task 7: Chạy kiểm thử build thực tế `gradlew.bat assembleDebug` + `testDebugUnitTest` và script kiểm tra `mobile_audit.py` → Verify: `BUILD SUCCESSFUL` không lỗi biên dịch.

## Done When
- [x] `demo/15_DemoWeather` build ra APK debug thành công (`BUILD SUCCESSFUL`), giao diện chuẩn Material, đầy đủ ID bài giảng, tích hợp `NetworkChangeReceiver` + fallback an toàn, và có tài liệu `demo/docs/15_DemoWeather.md`.
