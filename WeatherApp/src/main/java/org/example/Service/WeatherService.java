package com.example.weatherapp.service;

import com.example.weatherapp.model.WeatherData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final String apiKey ="2068715ee6c70f01debfa82701461083";
    private final String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather";
    private final String oneCallUrl = "https://api.openweathermap.org/data/3.0/onecall";

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public WeatherData getCurrentWeather(String city) {
        String url = currentWeatherUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return parseWeatherResponse(response.getBody(), city);
        } catch (Exception e) {
            throw new RuntimeException("API Error: " + e.getMessage());
        }
    }

    public List<WeatherData> get7DayForecast(String city) {
        // Step 1: Get coordinates from city name
        String geoUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey;
        ResponseEntity<String> geoResponse = restTemplate.getForEntity(geoUrl, String.class);
        JSONArray geoArray = new JSONArray(geoResponse.getBody());

        if (geoArray.isEmpty()) throw new RuntimeException("City not found");

        JSONObject location = geoArray.getJSONObject(0);
        double lat = location.getDouble("lat");
        double lon = location.getDouble("lon");

        // Step 2: Use OneCall API to fetch forecast
        String forecastUrl = oneCallUrl + "?lat=" + lat + "&lon=" + lon + "&exclude=hourly,minutely,alerts,current&units=metric&appid=" + apiKey;
        ResponseEntity<String> forecastResponse = restTemplate.getForEntity(forecastUrl, String.class);

        return parseForecastResponse(forecastResponse.getBody(), city);
    }

    private WeatherData parseWeatherResponse(String responseBody, String city) {
        JSONObject json = new JSONObject(responseBody);
        JSONObject main = json.getJSONObject("main");
        JSONObject wind = json.getJSONObject("wind");
        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);

        WeatherData data = new WeatherData();
        data.setCityName(city);
        data.setTemperature(main.getDouble("temp"));
        data.setHumidity(main.getInt("humidity"));
        data.setWindSpeed(wind.getDouble("speed"));
        data.setDescription(weather.getString("description"));
        data.setDate(LocalDateTime.now());

        return data;
    }

    private List<WeatherData> parseForecastResponse(String responseBody, String city) {
        JSONObject json = new JSONObject(responseBody);
        JSONArray dailyArray = json.getJSONArray("daily");

        List<WeatherData> forecastList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            JSONObject day = dailyArray.getJSONObject(i);
            JSONObject temp = day.getJSONObject("temp");
            JSONObject weather = day.getJSONArray("weather").getJSONObject(0);

            WeatherData data = new WeatherData();
            data.setCityName(city);
            data.setTemperature(temp.getDouble("day"));
            data.setHumidity(day.getInt("humidity"));
            data.setWindSpeed(day.getDouble("wind_speed"));
            data.setDescription(weather.getString("description"));
            data.setDate(LocalDateTime.ofEpochSecond(day.getLong("dt"), 0, ZoneOffset.UTC));

            forecastList.add(data);
        }

        return forecastList;
    }
}
