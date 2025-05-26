package com.example.WeatherApiClient;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApiClient {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public WeatherApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;}

    public String fetchCurrentWeather(String city) {
        String url = String.format("%s/weather?q=%s&units=metric&appid=%s", baseUrl, city, apiKey);
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            // Handle error or log it
            throw new RuntimeException("Failed to fetch current weather data", e);
        }
    }
    public String fetch7DayForecast(String city) {
        String url = String.format("%s/forecast/daily?q=%s&cnt=7&units=metric&appid=%s", baseUrl, city, apiKey);
        try {
            return restTemplate.getForObject(url, String.class);
        }
        catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch 7-day forecast", e);
        }
    }
}

