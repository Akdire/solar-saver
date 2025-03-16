package com.akilas.solarsaverprojectakilas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akilas.solarsaverprojectakilas.data.geocode.dataSource.GeocodeDataSource // Updated path
import com.akilas.solarsaverprojectakilas.data.geocode.repository.GeocodeRepository // Updated path
import com.akilas.solarsaverprojectakilas.model.Location // Import our custom Location
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val geocodeRepo = GeocodeRepository(GeocodeDataSource("your-maps-api-key-here"))
        setContent {
            SolarSavrApp(geocodeRepo)
        }
    }
}

// Main app composable with address input and coordinate display
@Composable
fun SolarSavrApp(repo: GeocodeRepository) {
    var address by remember { mutableStateOf("") }
    // Explicitly use our custom Location type
    var location by remember { mutableStateOf<com.akilas.solarsaverprojectakilas.model.Location?>(null) }
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
            Button(onClick = {
                scope.launch {
                    location = repo.getLocation(address)
                }
            }) {
                Text("Get Coordinates")
            }
            // Safely handle nullable custom Location
            location?.let {
                Text("Lat: ${it.lat}, Lng: ${it.lng}")
            } ?: Text("No location yet")
        }
    }
}