package com.example.demoweather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.demoweather.data.WeatherFallbackProvider;
import com.example.demoweather.model.WeatherForecastModel;

import org.junit.Test;

public class WeatherFallbackProviderTest {

    @Test
    public void buildForecastUrl_encodesCityWithSpaces() {
        // Arrange
        String apiKey = "test_key";
        String city = "Ho Chi Minh";

        // Act
        String url = WeatherFallbackProvider.buildForecastUrl(apiKey, city);

        // Assert
        assertTrue(url.contains("key=test_key"));
        assertTrue(url.contains("q=Ho%20Chi%20Minh"));
        assertTrue(url.contains("days=1&aqi=no&alerts=no"));
    }

    @Test
    public void createSampleWeather_generates24HourlyForecasts() {
        // Arrange
        String city = "Hanoi";

        // Act
        WeatherFallbackProvider.FallbackWeather fallback =
                WeatherFallbackProvider.createSampleWeather(city);

        // Assert
        assertEquals("Hanoi", fallback.getCityName());
        assertNotNull(fallback.getCurrentTemp());
        assertEquals(24, fallback.getHourlyList().size());
        assertNull(fallback.getHourlyList().get(0).getFullIconUrl());
    }

    @Test
    public void getFullIconUrl_prependsHttpsForProtocolRelativePath() {
        // Arrange
        WeatherForecastModel model = new WeatherForecastModel(
                "2026-11-01 09:00",
                "27.5",
                "//cdn.weatherapi.com/weather/64x64/day/116.png",
                "12.6"
        );

        // Act
        String fullUrl = model.getFullIconUrl();

        // Assert
        assertEquals("https://cdn.weatherapi.com/weather/64x64/day/116.png", fullUrl);
    }
}

