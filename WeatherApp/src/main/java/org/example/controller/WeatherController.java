package src.main.java.org.example.controller;

import com.example.weatherapp.model.WeatherData;
import com.example.weatherapp.service.WeatherService;
import com.example.weatherapp.component.SearchHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * GET /api/weather?city=Nairobi
     * Fetches current weather for the given city.
     */
    @GetMapping("/weather")
    public ResponseEntity<WeatherData> getCurrentWeather(@RequestParam String city) {
        searchHistory.addSearch(city); // Track the search
        WeatherData data = weatherService.getCurrentWeather(city);
        return ResponseEntity.ok(data);
    }

    /**
     * GET /api/search-history
     * Returns a list of recently searched cities.
     */
    @GetMapping("/search-history")
    public ResponseEntity<List<String>> getSearchHistory() {
        return ResponseEntity.ok(searchHistory.getRecentSearches());
    }

    /**
     * GET /api/forecast?city=Nairobi
     * Optional: Returns a basic forecast for the given city (dummy or placeholder).
     * You can implement this fully later with a ForecastCalendar class.
     */
    @GetMapping("/forecast")
    public ResponseEntity<Map<String, WeatherData>> getForecast(@RequestParam String city) {
        Map<String, WeatherData> forecast = weatherService.get7DayForecast(city);
        return ResponseEntity.ok(forecast);
    }
}
