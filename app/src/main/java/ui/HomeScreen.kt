package com.example.myapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToWeather: () -> Unit,
    onNavigateToKNMI: () -> Unit,
    onNavigateToUSGS: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hobby Weer App",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Deltion-opdracht: weer + aardbevingen NL + aardbevingen wereldwijd",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onNavigateToWeather) {
            Text("Weerdata (OpenWeatherMap)")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onNavigateToKNMI) {
            Text("Aardbevingen NL (KNMI)")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onNavigateToUSGS) {
            Text("Aardbevingen wereldwijd (USGS)")
        }
    }
}
