# 15_DemoWeather Plan

## Goal
Xây dựng hoàn chỉnh dự án Android `demo/15_DemoWeather` bám sát 100% tài liệu của thầy (`Layout_Demo_Netwwork_Android.pdf`, `DemoWeatherAPI.pdf`, `ic_baseline_search_24.xml`, `NetworkingAndroid`) kèm tài liệu ôn tập vấn đáp `demo/docs/15_DemoWeather.md`.

## Tasks
- [x] Task 1: Khởi tạo cấu trúc Gradle dự án `demo/15_DemoWeather` chuẩn Android Studio (`jbr-25`, `agp = "9.3.3"`) với `volley:1.2.1` và `picasso:2.8`.
- [x] Task 2: Cấu hình `AndroidManifest.xml` cấp quyền `INTERNET` và thiết lập tài nguyên `colors.xml`, `strings.xml`, `themes.xml`, `ic_baseline_search_24.xml` (`#000000`) giống hệt tài liệu.
- [x] Task 3: Thiết kế `activity_main.xml` và `layout_weather_items.xml` giống hệt 100% `Layout_Demo_Netwwork_Android.pdf`.
- [x] Task 4: Cài đặt `WeatherForecastModel`, `WeatherAdapter` và `MainActivity` (`GetData`) đúng chuẩn `DemoWeatherAPI.pdf`.
- [x] Task 5: Cập nhật tài liệu `demo/docs/15_DemoWeather.md` và `demo/docs/README.md` đồng bộ 100% với code.
- [x] Task 6: Chạy kiểm thử build thực tế `gradlew.bat assembleDebug testDebugUnitTest` và `mobile_audit.py` → `BUILD SUCCESSFUL`.
