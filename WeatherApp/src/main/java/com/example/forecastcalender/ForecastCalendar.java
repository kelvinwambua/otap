package com.example.forecastcalender;

import com.example.model.WeatherData;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public class ForecastCalendar {
    private final Map<DayOfWeek, WeatherData> forecastData;

    public ForecastCalendar() {
        this.forecastData = new HashMap<>();
    }

    // Add forecast data for a specific day
    public void addForecastData(DayOfWeek day, WeatherData data) {
        forecastData.put(day, data);
    }

    // Get forecast for a specific day
    public WeatherData getForecastForDay(DayOfWeek day) {
        return forecastData.get(day);
    }

    // Get all forecasts
    public Map<DayOfWeek, WeatherData> getAllForecasts() {
        return forecastData;
    }
}
