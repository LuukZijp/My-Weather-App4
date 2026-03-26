package com.example.myapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    onBack: () -> Unit,
    viewModel: WeatherViewModel = viewModel()
) {
    var city by remember { mutableStateOf("Amsterdam") }
    val weather = viewModel.weatherState.collectAsState()
    val loading = viewModel.isLoading.collectAsState()
    val error = viewModel.errorMessage.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val isWide = maxWidth > 700.dp

        if (isWide) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Header(onBack = onBack)
                    QueryControls(
                        city = city,
                        onCityChange = { city = it },
                        onLoad = { viewModel.loadWeather(city) },
                        loading = loading.value,
                        error = error.value
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1f)) {
                    WeatherDetails(weather.value)
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Header(onBack = onBack)
                QueryControls(
                    city = city,
                    onCityChange = { city = it },
                    onLoad = { viewModel.loadWeather(city) },
                    loading = loading.value,
                    error = error.value
                )
                WeatherDetails(weather.value)
            }
        }
    }
}

@Composable
private fun Header(onBack: () -> Unit) {
    Button(onClick = onBack) {
        Text("Terug")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Weerdata (OpenWeatherMap)",
        style = MaterialTheme.typography.headlineSmall
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun QueryControls(
    city: String,
    onCityChange: (String) -> Unit,
    onLoad: () -> Unit,
    loading: Boolean,
    error: String?
) {
    TextField(
        value = city,
        onValueChange = onCityChange,
        label = { Text("Voer een stad in") }
    )

    Spacer(modifier = Modifier.height(12.dp))

    Button(onClick = onLoad, enabled = !loading) {
        Text(if (loading) "Laden..." else "Laad weer")
    }

    if (!error.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(error, color = MaterialTheme.colorScheme.error)
    }

    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun WeatherDetails(weather: com.example.myapp.data.WeatherResponse?) {
    if (weather == null) {
        Text("Nog geen weerdata geladen.")
        return
    }

    val condition = weather.weather.firstOrNull()

    Text("Stad: ${weather.name}", style = MaterialTheme.typography.bodyLarge)
    Text("Temperatuur: ${weather.main.temp} °C", style = MaterialTheme.typography.bodyLarge)
    Text("Wind: ${weather.wind.speed} m/s", style = MaterialTheme.typography.bodyLarge)
    Text("Luchtvochtigheid: ${weather.main.humidity}%", style = MaterialTheme.typography.bodyLarge)
    Text("Luchtdruk: ${weather.main.pressure} hPa", style = MaterialTheme.typography.bodyLarge)

    if (condition != null) {
        Text("Conditie: ${condition.main} (${condition.description})", style = MaterialTheme.typography.bodyLarge)
    }
}
