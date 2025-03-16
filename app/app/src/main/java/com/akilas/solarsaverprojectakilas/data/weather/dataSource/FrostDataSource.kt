package com.akilas.solarsaverprojectakilas.data.weather.dataSource


import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.akilas.solarsaverprojectakilas.data.weather.model.LocationInfo
import com.akilas.solarsaverprojectakilas.data.weather.model.MetaDataLocation
import com.akilas.solarsaverprojectakilas.data.weather.model.ObservationInfo
import com.akilas.solarsaverprojectakilas.data.weather.model.OutputObservation

// Data source for Frost-API
class FrostDatasource(private val clientId: String, private val clientPassword: String) {

    private val ktorHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        // Need Auth with clientId to make get-requests to Frost
        install(Auth) {
            bearer { // Frost now prefers Bearer over Basic
                loadTokens {
                    BearerTokens(clientId, "")
                }

            }
        }
    }

    // Function to get the location-source IDs, which we can use for observation requests
    suspend fun getLocationInfo(
        longitude: Double,
        latitude: Double,
        timeframe: String = "now"
    ): List<LocationInfo> {

        // Make the timeframe into the correct format
        val urlEncodedTimeframe = timeframe.replace("/", "%2F")

        // If we want current-date data, we use the regular elements
        val elements = if (timeframe == "now") {
            listOf(
                "air_temperature",
                "cloud_area_fraction",
                "snow_depth"
            ).joinToString(separator = "%2C%20")
            // If we want historical data, we use mean-values, to lower the response-size
        } else {
            listOf(
                "mean(air_temperature%20P1M)",
                "mean(cloud_area_fraction%20P1M)",
                "mean(snow_depth%20P1M)"
            ).joinToString(separator = "%2C%20")
        }

        val url =
            "https://frost.met.no/sources/v0.jsonld?types=SensorSystem&elements=$elements&geometry=nearest(POINT($longitude%20$latitude))&nearestmaxcount=10&validtime=$urlEncodedTimeframe&fields=id"

        // Return the "data" value, which includes the "source" values
        return try {
            val serializedResponse: MetaDataLocation = ktorHttpClient.get(url).body()
            serializedResponse.data
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    // Function to get the actual observations, using the list of sources "ids"
    suspend fun getObservations(
        ids: List<String>,
        timeframe: String = "latest"
    ): List<ObservationInfo> {

        val elements = if (timeframe == "latest") {
            listOf(
                "air_temperature",
                "cloud_area_fraction",
                "snow_depth"
            ).joinToString(separator = "%2C%20")
        } else {
            listOf(
                "mean(air_temperature%20P1M)",
                "mean(cloud_area_fraction%20P1M)",
                "mean(snow_depth%20P1M)"
            ).joinToString(separator = "%2C%20")
        }

        // Make the list of ids into correct format
        val joinedIds = ids.joinToString(separator = ",")
        val urlEncodedIds = joinedIds.replace(",", "%2C")

        // Make timeframe into correct format as well
        val urlEncodedTimeframe = timeframe.replace("/", "%2F")

        val url =
            "https://frost.met.no/observations/v0.jsonld?sources=$urlEncodedIds&referencetime=$urlEncodedTimeframe&elements=$elements&fields=referenceTime%2C%20value%2C%20elementId"

        // Return a list of observationInfo-items
        return try {
            val serializedResponse: OutputObservation = ktorHttpClient.get(url).body()
            serializedResponse.data.flatMap { it.observations }

        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}
