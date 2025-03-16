package com.akilas.solarsaverprojectakilas.data.geocode.dataSource

import com.akilas.solarsaverprojectakilas.model.GeocodeResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GeocodeDataSource(private val apiKey: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }) // Ignore extra fields like error_message
        }
    }

    // Fetch coordinates from Google Maps Geocoding API
    suspend fun getCoordinates(address: String): GeocodeResponse {
        return client.get("https://maps.googleapis.com/maps/api/geocode/json") {
            parameter("address", address)
            parameter("key", apiKey)
        }.body()
    }
}