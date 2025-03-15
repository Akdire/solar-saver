package com.akilas.solarsaverprojectakilas.data.radiation.dataSource


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.akilas.solarsaverprojectakilas.data.radiation.model.MonthlyRadiation
import com.akilas.solarsaverprojectakilas.data.radiation.model.RadiationOutputs



class RadiationDatasource {

    private val ktorHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun getRadiationForLocation(
        latitude: Double,
        longitude: Double
    ): List<MonthlyRadiation> {

        val url =
            "https://re.jrc.ec.europa.eu/api/MRcalc?lat=$latitude&lon=$longitude&horirrad=1&outputformat=json&startyear=2020"

        return try {
            val serializedResponse: RadiationOutputs = ktorHttpClient.get(url).body()
            serializedResponse.outputs.monthly
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
