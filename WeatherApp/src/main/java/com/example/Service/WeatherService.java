package com.example.Service;

import com.example.model.WeatherData;
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
    private final String forecastUrl = "https://api.openweathermap.org/data/2.5/forecast";


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
        String url = forecastUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());
            JSONArray list = json.getJSONArray("list");

            List<WeatherData> dailyForecast = new ArrayList<>();

            // Pick roughly 1 entry per day at midday (index: 4, 12, 20, 28, 36...)
            for (int i = 4; i < list.length() && dailyForecast.size() < 5; i += 8) {
                JSONObject entry = list.getJSONObject(i);
                JSONObject main = entry.getJSONObject("main");
                JSONObject wind = entry.getJSONObject("wind");
                JSONObject weather = entry.getJSONArray("weather").getJSONObject(0);

                WeatherData data = new WeatherData();
                data.setCityName(city);
                data.setTemperature(main.getDouble("temp"));
                data.setHumidity(main.getInt("humidity"));
                data.setWindSpeed(wind.getDouble("speed"));
                data.setDescription(weather.getString("description"));

                long timestamp = entry.getLong("dt");
                data.setDate(LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC));

                dailyForecast.add(data);
            }

            return dailyForecast;

        } catch (org.springframework.web.client.HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Invalid API key. Please check your OpenWeatherMap key.");
        } catch (Exception e) {
            throw new RuntimeException("Error fetching forecast: " + e.getMessage());
        }
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
