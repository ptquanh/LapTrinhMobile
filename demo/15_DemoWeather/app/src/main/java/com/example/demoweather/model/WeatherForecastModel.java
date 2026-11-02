package com.example.demoweather.model;

public class WeatherForecastModel {
    private String time;
    private String temperature;
    private String icon;
    private String windSpeed;

    public WeatherForecastModel(String time, String temperature, String icon, String windSpeed) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getFullIconUrl() {
        if (icon == null || icon.trim().isEmpty()) {
            return null;
        }
        String trimmed = icon.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }
        if (trimmed.startsWith("//")) {
            return "https:" + trimmed;
        }
        return "https://" + trimmed;
    }
}

