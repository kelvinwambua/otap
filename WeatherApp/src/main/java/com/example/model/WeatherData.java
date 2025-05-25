package com.example.model;

import java.time.LocalDateTime;

public class WeatherData {
    private double temperature; // the temp in cels
    private int humidity;
    private double windSpeed; // speed in metres per second
    private String description;
    private LocalDateTime date;
    private String cityName;

    public WeatherData() {
        // empty constructor
    }

    // Constructor
    public WeatherData(double temperature, int humidity, double windSpeed, String description, LocalDateTime date, String cityName) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.description = description;
        this.date = date;
        this.cityName = cityName;
    }

    // Getters and Setters
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // Convert temperature to desired unit
    public double convertTemperature(String unit) {
        if (unit.equalsIgnoreCase("Fahrenheit")) {
            return (temperature * 9 / 5) + 32;
        } else if (unit.equalsIgnoreCase("Celsius")) {
            return temperature;
        } else {
            throw new IllegalArgumentException("Unsupported temperature unit: " + unit);
        }
    }

    // Simple utility method to check if rain is likely
    public boolean isRainLikely() {
        String lowerDesc = description.toLowerCase();
        return lowerDesc.contains("rain") || lowerDesc.contains("storm") || lowerDesc.contains("shower");
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature + "°C" +
                ", humidity=" + humidity + "%" +
                ", windSpeed=" + windSpeed + " m/s" +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
