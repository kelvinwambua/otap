async function fetchCurrentWeather() {
    const city = document.getElementById("cityInput").value.trim();
    if (!city) return alert("Please enter a city.");

    try {
        const response = await fetch(`/api/weather?city=${city}`);
        if (!response.ok) throw new Error("City not found or API error");

        const data = await response.json();

        const html = `
<strong>City:</strong> ${data.cityName}<br>
<strong>Temperature:</strong> ${data.temperature}°C<br>
<strong>Humidity:</strong> ${data.humidity}%<br>
<strong>Wind Speed:</strong> ${data.windSpeed} m/s<br>
<strong>Description:</strong> ${data.description}<br>
<strong>Date:</strong> ${data.date}
        `;

        document.getElementById("currentWeather").innerHTML = html;

    } catch (error) {
        document.getElementById("currentWeather").innerText = "Error: " + error.message;
    }
}

async function fetchForecast() {
    const city = document.getElementById("cityInput").value.trim();
    if (!city) return alert("Please enter a city.");

    try {
        const response = await fetch(`/api/forecast?city=${city}`);
        if (!response.ok) throw new Error("City not found or API error");

        const forecastData = await response.json();
        const container = document.getElementById("forecastContainer");

        container.innerHTML = ""; // clear old results

        const days = Array.isArray(forecastData)
            ? forecastData
            : Object.entries(forecastData).map(([day, data]) => ({ ...data, day }));

        days.forEach(day => {
            const card = document.createElement("div");
            card.className = "card";
            card.innerHTML = `
<strong>${day.day || day.date || "Day"}</strong><br>
<strong>Temp:</strong> ${day.temperature}°C<br>
<strong>Humidity:</strong> ${day.humidity}%<br>
<strong>Wind:</strong> ${day.windSpeed} m/s<br>
<strong>Condition:</strong> ${day.description}<br>
<strong>Date:</strong> ${day.date}
            `;
            container.appendChild(card);
        });

    } catch (error) {
        document.getElementById("forecastContainer").innerText = "Error: " + error.message;
    }
}
