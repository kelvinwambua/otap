package com.example.controller;

import com.example.model.WeatherData;
import com.example.Service.WeatherService;
import com.example.history.SearchHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;
    private final SearchHistory searchHistory;

    // Constructor Injection
    public WeatherController(WeatherService weatherService, SearchHistory searchHistory) {
        this.weatherService = weatherService;
        this.searchHistory = searchHistory;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherData> getCurrentWeather(@RequestParam String city) {
        searchHistory.addSearch(city); // Track the search
        WeatherData data = weatherService.getCurrentWeather(city);
        return ResponseEntity.ok(data);
    }


    @GetMapping("/search-history")
    public ResponseEntity<List<String>> getSearchHistory() {
        return ResponseEntity.ok(searchHistory.getRecentSearches());
    }


    @GetMapping("/forecast")
    public ResponseEntity<Map<String, WeatherData>> getForecast(@RequestParam String city) {
        List<WeatherData> forecastList = weatherService.get7DayForecast(city);

        Map<String, WeatherData> forecastMap = new LinkedHashMap<>();
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        for (int i = 0; i < forecastList.size() && i < dayNames.length; i++) {
            forecastMap.put(dayNames[i], forecastList.get(i));
        }

        return ResponseEntity.ok(forecastMap);
    }

}
