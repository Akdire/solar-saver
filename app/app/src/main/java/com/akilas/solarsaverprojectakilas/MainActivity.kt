package com.akilas.solarsaverprojectakilas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akilas.solarsaverprojectakilas.data.geocode.dataSource.GeocodeDataSource
import com.akilas.solarsaverprojectakilas.data.geocode.repository.GeocodeRepository
import com.akilas.solarsaverprojectakilas.data.radiation.repository.RadiationRepository
import com.akilas.solarsaverprojectakilas.data.weather.repository.WeatherRepository
import com.akilas.solarsaverprojectakilas.model.Location
import com.akilas.solarsaverprojectakilas.util.SolarCalculator
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val geocodeRepo = GeocodeRepository(GeocodeDataSource("AIzaSyCfaxHBIZb_Dr_KHggZi6pIFqJo-B6MWL8"))
        val weatherRepo = WeatherRepository() // Uses team’s Frost creds
        val radiationRepo = RadiationRepository()
        setContent {
            SolarSavrApp(geocodeRepo, weatherRepo, radiationRepo)
        }
    }
}

// Main UI with address input and data display
@Composable
fun SolarSavrApp(
    geocodeRepo: GeocodeRepository,
    weatherRepo: WeatherRepository,
    radiationRepo: RadiationRepository
) {
    var address by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<Location?>(null) }
    var weather by remember { mutableStateOf<List<Double>?>(null) }
    var radiation by remember { mutableStateOf<List<Double>?>(null) }
    var kwhResult by remember { mutableStateOf<Double?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Enter Address") },
                modifier = Modifier.fillMaxWidth()
            )
            // Fetch all data on button click
            Button(onClick = {
                scope.launch {
                    try {
                        val loc = geocodeRepo.getLocation(address)
                        location = loc
                        if (loc != null) {
                            val weatherData = weatherRepo.getHistoricalAverageWeatherData(loc.lat, loc.lng, "2024-01-01/2024-12-31")
                            val radiationData = radiationRepo.getRadiationData(loc.lat, loc.lng)
                            weather = weatherData
                            radiation = radiationData
                            if (weatherData.isNotEmpty() && radiationData.isNotEmpty()) {
                                kwhResult = SolarCalculator.calculateKwh(
                                    radiationData[0],
                                    weatherData[0],
                                    weatherData[1],
                                    weatherData[2]
                                )
                            } else {
                                error = "Missing data for calculation"
                            }
                        }
                        error = if (loc == null) "No location found" else null
                    } catch (e: Exception) {
                        Log.e("SolarSavr", "Failed: ${e::class.simpleName} - ${e.message}", e)
                        error = "Error: ${e::class.simpleName ?: "Unknown"} - ${e.message ?: "No details"}"
                        location = null; weather = null; radiation = null; kwhResult = null
                    }
                }
            }) { Text("Get Data") }

            location?.let { Text("Lat: ${it.lat}, Lng: ${it.lng}") }
            weather?.let { Text("Temp: ${it[0]}°C, Clouds: ${it[1]}, Snow: ${it[2]}") }
            radiation?.let { Text("Avg Radiation: ${it[0]} kWh/m²") }
            kwhResult?.let { Text("Yearly kWh: $it") }
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) } ?: Text(if (location == null) "No data yet" else "")
        }
    }
}