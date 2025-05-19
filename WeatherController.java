package com.example.demo.Controller;

import com.example.demo.model.WeatherResponse;
import com.example.demo.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/weather")
    public String showWeather(Model model) {
        model.addAttribute("weather", new WeatherResponse()); // Ensuring a non-null object
        return "weather";
    }

    @GetMapping("/getWeather")
    public String getWeather(@RequestParam(required = false) String city, Model model) {
        if (city == null || city.trim().isEmpty()) {
            logger.warn("User submitted an empty city name.");
            model.addAttribute("error", "City name cannot be empty.");
            model.addAttribute("weather", new WeatherResponse()); // Prevents Thymeleaf errors
            return "weather";
        }

        city = city.trim(); // Trim spaces from user input

        try {
            WeatherResponse weatherResponse = weatherService.getWeather(city);

            if (weatherResponse == null || 
                weatherResponse.getWeather() == null || 
                weatherResponse.getWeather().isEmpty()) {

                logger.warn("No weather data found for city: {}", city);
                model.addAttribute("error", "Weather data not found for this city.");
                model.addAttribute("weather", new WeatherResponse()); // Ensure Thymeleaf doesn't break
            } else {
                logger.info("Successfully fetched weather data for city: {}", city);
                model.addAttribute("weather", weatherResponse);
            }
        } catch (Exception e) {
            logger.error("Error fetching weather data for city {}: {}", city, e.getMessage());
            model.addAttribute("error", "Could not fetch weather data. Please try again.");
            model.addAttribute("weather", new WeatherResponse()); // Default empty object
        }

        return "weather";
    }
}
