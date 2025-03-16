package com.akilas.solarsaverprojectakilas.data.weather.repository

import com.akilas.solarsaverprojectakilas.data.weather.dataSource.FrostDatasource

class WeatherRepository {
    private val datasource = FrostDatasource(
        clientId = "c9bb4306-6e35-4784-852d-cd45ac9cab21 ", // my clientId  (akil) with frost
        clientPassword = "ee3f0b33-8a33-48dc-940d-02fb20530679" // Frost uses clientId only now; password optional
    )

    // Current weather data (simplified)
    suspend fun getCurrentWeatherData(longitude: Double, latitude: Double): List<Double> {
        val locationIds = datasource.getLocationInfo(longitude, latitude).map { it.id }
        val observations = datasource.getObservations(locationIds)
        return listOf(
            observations.filter { it.elementId == "air_temperature" }.map { it.value }.average(),
            observations.filter { it.elementId == "cloud_area_fraction" }.map { it.value }.average(),
            observations.filter { it.elementId == "snow_depth" }.map { it.value }.average().takeIf { it.isFinite() } ?: 0.0
        )
    }

    // Historical averages for a timeframe
    suspend fun getHistoricalAverageWeatherData(longitude: Double, latitude: Double, timeframe: String): List<Double> {
        val locationIds = datasource.getLocationInfo(longitude, latitude, timeframe).map { it.id }
        val observations = datasource.getObservations(locationIds, timeframe)
        return listOf(
            observations.filter { it.elementId == "mean(air_temperature P1M)" }.map { it.value }.average(),
            observations.filter { it.elementId == "mean(cloud_area_fraction P1M)" }.map { it.value }.average(),
            observations.filter { it.elementId == "mean(snow_depth P1M)" }.map { it.value }.average().takeIf { it.isFinite() } ?: 0.0
        )
    }
}