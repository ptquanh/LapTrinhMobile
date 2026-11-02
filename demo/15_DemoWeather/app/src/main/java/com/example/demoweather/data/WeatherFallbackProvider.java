package com.example.demoweather.data;

import com.example.demoweather.model.WeatherForecastModel;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class WeatherFallbackProvider {

    public static final String DEFAULT_API_KEY = "fc8f4188ad3f48d8a10132707221212";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/forecast.json";

    private WeatherFallbackProvider() {
    }

    public static class FallbackWeather {
        private final String cityName;
        private final String currentTemp;
        private final String conditionText;
        private final List<WeatherForecastModel> hourlyList;

        public FallbackWeather(
                String cityName,
                String currentTemp,
                String conditionText,
                List<WeatherForecastModel> hourlyList
        ) {
            this.cityName = cityName;
            this.currentTemp = currentTemp;
            this.conditionText = conditionText;
            this.hourlyList = hourlyList;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCurrentTemp() {
            return currentTemp;
        }

        public String getConditionText() {
            return conditionText;
        }

        public List<WeatherForecastModel> getHourlyList() {
            return hourlyList;
        }
    }

    public static String buildForecastUrl(String apiKey, String cityName) {
        String cleanCity = (cityName == null || cityName.trim().isEmpty())
                ? "Hanoi"
                : cityName.trim();
        String encodedCity = URLEncoder.encode(cleanCity, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return BASE_URL
                + "?key=" + apiKey
                + "&q=" + encodedCity
                + "&days=1&aqi=no&alerts=no";
    }

    public static FallbackWeather createSampleWeather(String requestedCity) {
        String displayCity = (requestedCity == null || requestedCity.trim().isEmpty())
                ? "Hanoi"
                : requestedCity.trim();

        String normalized = displayCity.toLowerCase(Locale.ROOT);
        double baseTemp = 26.5;
        String condition = "Partly cloudy (Dữ liệu mẫu)";

        if (normalized.contains("ho chi minh") || normalized.contains("saigon") || normalized.contains("hcm")) {
            baseTemp = 32.0;
            condition = "Sunny & Warm (Dữ liệu mẫu)";
        } else if (normalized.contains("london")) {
            baseTemp = 11.5;
            condition = "Light rain / Overcast (Dữ liệu mẫu)";
        } else if (normalized.contains("da lat") || normalized.contains("dalat")) {
            baseTemp = 18.0;
            condition = "Cool Mist (Dữ liệu mẫu)";
        }

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        List<WeatherForecastModel> hours = new ArrayList<>(24);

        for (int hour = 0; hour < 24; hour++) {
            double curve = Math.sin(((hour - 6) / 24.0) * 2.0 * Math.PI) * 3.5;
            double hourTemp = baseTemp + curve;
            double windSpeed = 8.5 + (hour % 6) * 1.8;

            String timeStr = String.format(Locale.US, "%s %02d:00", today, hour);
            String tempStr = String.format(Locale.US, "%.1f", hourTemp);
            String windStr = String.format(Locale.US, "%.1f", windSpeed);

            hours.add(new WeatherForecastModel(timeStr, tempStr, "", windStr));
        }

        String currentTempStr = String.format(Locale.US, "%.1f", baseTemp);
        return new FallbackWeather(displayCity, currentTempStr, condition, hours);
    }
}

