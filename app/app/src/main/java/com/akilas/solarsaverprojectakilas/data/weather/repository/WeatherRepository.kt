package com.akilas.solarsaverprojectakilas.data.weather.repository

import com.akilas.solarsaverprojectakilas.data.weather.dataSource.FrostDatasource


// Frost-api repository
class WeatherRepository {

    // Using my (akil) clientId for authentication to the API
    private val datasource = FrostDatasource(
        clientId = "c9bb4306-6e35-4784-852d-cd45ac9cab21",
        clientPassword = "ee3f0b33-8a33-48dc-940d-02fb20530679"
    )

    /*
       This function returns data for the current date, as averages amongst all the observations.
       It will return a list of size 3, where:
       - The first index represents temperature in Celsius
       - The second index represents "cloudiness" in fractions (0-8), where 0 means no clouds.
       - The third index represents snow on ground level (which we can assume will also be on the roof)
         with values 0-4, where 0 means no snow, and 4 means the ground is covered in snow.
    */
    suspend fun getCurrentWeatherData(longitude: Double, latitude: Double): List<Double> {

        val locationIds = datasource.getLocationInfo(longitude, latitude).map { it.id }
        val observations = datasource.getObservations(locationIds)

        val temperatureDataAverage =
            if (observations.none { it.elementId == "air_temperature" }) 0.0
            else observations.filter { it.elementId == "air_temperature" }.map { it.value }
                .average()

        val cloudDataAverage = if (observations.none { it.elementId == "cloud_area_fraction" }) 0.0
        else observations.filter { it.elementId == "cloud_area_fraction" }.map { it.value }
            .average()

        val snowDataAverage = if (observations.none { it.elementId == "snow_coverage_type" }) 0.0
        else observations.filter { it.elementId == "snow_coverage_type" }.map { it.value }.average()


        return listOf(temperatureDataAverage, cloudDataAverage, snowDataAverage)
    }

    // This function does the same, but retrieves averages among observations over a certain amount of time
    suspend fun getHistoricalAverageWeatherData(
        longitude: Double,
        latitude: Double,
        timeframe: String
    ): List<Double> {

        val locationIds = datasource.getLocationInfo(longitude, latitude, timeframe).map { it.id }
        val observations = datasource.getObservations(locationIds, timeframe)

        val temperatureDataAverage =
            observations.filter { it.elementId == "mean(air_temperature P1M)" }.map { it.value }
                .average()
        val cloudDataAverage =
            observations.filter { it.elementId == "mean(cloud_area_fraction P1M)" }.map { it.value }
                .average()
        val snowDataAverage =
            observations.filter { it.elementId == "mean(snow_coverage_type P1M)" }.map { it.value }
                .average()

        return listOf(temperatureDataAverage, cloudDataAverage, snowDataAverage)
    }
}
