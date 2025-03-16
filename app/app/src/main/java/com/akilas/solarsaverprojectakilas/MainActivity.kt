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
import com.akilas.solarsaverprojectakilas.model.Location
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val geocodeRepo = GeocodeRepository(GeocodeDataSource("AIzaSyCfaxHBIZb_Dr_KHggZi6pIFqJo-B6MWL8"))
        setContent {
            SolarSavrApp(geocodeRepo)
        }
    }
}

// Main app UI with address input and error handling
@Composable
fun SolarSavrApp(repo: GeocodeRepository) {
    var address by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<Location?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Enter Address") },
                modifier = Modifier.fillMaxWidth()
            )
            // Button to fetch coordinates
            Button(onClick = {
                scope.launch {
                    try {
                        val result = repo.getLocation(address) // Fetch location
                        location = result
                        error = if (result == null) "No location found" else null
                    } catch (e: Exception) {
                        Log.e("SolarSavr", "Geocode failed: ${e::class.simpleName} - ${e.message}", e)
                        error = "Error: ${e::class.simpleName ?: "Unknown"} - ${e.message ?: "No details"}"
                        location = null
                    }
                }
            }) {
                Text("Get Coordinates")
            }
            // Display results or error
            location?.let {
                Text("Lat: ${it.lat}, Lng: ${it.lng}")
            }
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
                ?: Text(if (location == null) "No location yet" else "") // Fixed: if as expression        }
        }
    }
}