package com.example.history;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SearchHistory {

    private final List<String> historyList = new ArrayList<>();

    /**
     * Adds a city to the search history.
     * Duplicate entries are ignored for simplicity.
     */
    public void addSearch(String city) {
        if (city != null && !city.trim().isEmpty() && !historyList.contains(city)) {
            historyList.add(city);
        }
    }

    /**
     * Returns the list of recently searched cities.
     */
    public List<String> getRecentSearches() {
        return Collections.unmodifiableList(historyList);
    }

    /**
     * Optional: Clears the search history.
     */
    public void clearHistory() {
        historyList.clear();
    }
}

