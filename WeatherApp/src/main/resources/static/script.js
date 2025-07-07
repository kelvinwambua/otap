function searchWeather() {
    const city = document.getElementById("cityInput").value;

    // Get current weather
    fetch(`http://localhost:8080/api/weather?city=${city}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Current weather data not found.");
            }
            return response.json();
        })
        .then(currentData => {
            updateWeatherUI(currentData); // Your frontend method
        })
        .catch(error => {
            console.error("Error fetching current weather:", error);
            alert("Could not get current weather.");
        });

    // Get forecast
    fetch(`http://localhost:8080/api/forecast?city=${city}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Forecast data not found.");
            }
            return response.json();
        })
        .then(forecastData => {
            // Convert forecastData (object of days) into array
            const forecastArray = Object.keys(forecastData).map(day => {
                const data = forecastData[day];
                return {
                    day,
                    date: new Date(data.date).toDateString(),
                    temperature: `${Math.round(data.temperature)}°C`,
                    low: `Min ${Math.round(data.temperature - 3)}°C`, // just example logic
                    description: data.description,
                    humidity: `${data.humidity}%`,
                    windSpeed: `${Math.round(data.windSpeed)} km/h`
                };
            });
            renderForecast(forecastArray);
            renderDayDetails(forecastArray[0], 0);
        })
        .catch(error => {
            console.error("Error fetching forecast:", error);
            alert("Could not get forecast data.");
        });
}


function updateWeatherUI(current) {
    document.getElementById("cityName").innerText = current.cityName;
    document.getElementById("temperature").innerText = `${current.temperature}°C`;
    document.getElementById("condition").innerText = current.description;
    document.getElementById("humidity").innerText = `${current.humidity}%`;
    document.getElementById("wind").innerText = `${current.windSpeed} km/h`;
    document.getElementById("updated").innerText = new Date(current.date).toLocaleTimeString();
}

const forecastCards = document.getElementById("forecastCards");
const dayDetails = document.getElementById("dayDetails");

function renderForecast(forecastData) {
    forecastCards.innerHTML = "";

    forecastData.slice(0, 5).forEach((data, index) => {
        const dateObj = new Date(data.date);
        const card = document.createElement("div");
        card.className = "card" + (index === 0 ? " active" : ""); // Highlight today

        card.innerHTML = `
            <h4>${dateObj.toLocaleDateString(undefined, { weekday: 'short' })}</h4>
            <p>${dateObj.toLocaleDateString(undefined, { month: 'short', day: 'numeric' })}</p>
            <p>${data.description}</p>
            <h3>${data.temperature}°C</h3>
            <small>Humidity: ${data.humidity}%</small>
        `;

        card.onclick = () => renderDayDetails(data, index);
        forecastCards.appendChild(card);
    });

    // Load first day's details
    if (forecastData.length > 0) renderDayDetails(forecastData[0], 0);
}

function renderDayDetails(data, index) {
    document.querySelectorAll(".forecast-cards .card").forEach(card => card.classList.remove("active"));
    const cards = document.querySelectorAll(".forecast-cards .card");
    if (cards[index]) cards[index].classList.add("active");

    const date = new Date(data.date);

    dayDetails.innerHTML = `
        <h3>${date.toLocaleDateString(undefined, { weekday: 'long', month: 'long', day: 'numeric' })}</h3>
        <p><strong>Temperature:</strong> ${data.temperature}°C</p>
        <p><strong>Condition:</strong> ${data.description}</p>
        <p><strong>Humidity:</strong> ${data.humidity}%</p>
        <p><strong>Wind Speed:</strong> ${data.windSpeed} km/h</p>
    `;
}


