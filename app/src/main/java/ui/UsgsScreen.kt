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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.data.EarthquakeGlobal
import com.example.myapp.viewmodel.UsgsViewModel

@Composable
fun UsgsScreen(
    onBack: () -> Unit,
    viewModel: UsgsViewModel = viewModel()
) {
    val quakes = viewModel.quakes.collectAsState()
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
                    UsgsControls(
                        onBack = onBack,
                        onLoad = { viewModel.loadEarthquakes() },
                        loading = loading.value,
                        error = error.value
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1f)) {
                    UsgsList(quakes.value)
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                UsgsControls(
                    onBack = onBack,
                    onLoad = { viewModel.loadEarthquakes() },
                    loading = loading.value,
                    error = error.value
                )
                UsgsList(quakes.value)
            }
        }
    }
}

@Composable
private fun UsgsControls(
    onBack: () -> Unit,
    onLoad: () -> Unit,
    loading: Boolean,
    error: String?
) {
    Button(onClick = onBack) {
        Text("Terug")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text("Aardbevingen wereldwijd (USGS)", style = MaterialTheme.typography.headlineSmall)

    Spacer(modifier = Modifier.height(12.dp))

    Button(onClick = onLoad, enabled = !loading) {
        Text(if (loading) "Laden..." else "Laad wereldwijde aardbevingen")
    }

    if (!error.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(error, color = MaterialTheme.colorScheme.error)
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun UsgsList(items: List<EarthquakeGlobal>) {
    if (items.isEmpty()) {
        Text("Nog geen USGS-data geladen.")
    } else {
        LazyColumn {
            items(items.take(100)) { quake ->
                UsgsItem(quake)
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun UsgsItem(quake: EarthquakeGlobal) {
    Text("Titel: ${quake.title}")
    Text("Magnitude: ${quake.magnitude ?: 0.0}")
    Text("Updated: ${quake.updated}")
    Text("Coordinaten: ${quake.point}")
    Text("Link: ${quake.link}")
    Text("Samenvatting: ${quake.summary}")
}
