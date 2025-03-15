package com.akilas.solarsaverprojectakilas.data.radiation.repository

import com.akilas.solarsaverprojectakilas.data.radiation.dataSource.RadiationDatasource
import com.akilas.solarsaverprojectakilas.data.radiation.model.MonthlyRadiation



// Repo for radiation data from PVGIS
class RadiationRepository {

    private val datasource: RadiationDatasource = RadiationDatasource()

    // Returns a list of average radiations in kW/m^2/month ; Total, summertime and wintertime over multiple years
    suspend fun getRadiationData(latitude: Double, longitude: Double): List<Double>{
        val radiationResults = datasource.getRadiationForLocation(latitude, longitude)

        // Calculate the historically average radiation for the location
        return calculateAverage(radiationResults)
    }

    // Returns the average radiations in kW/m^2 for the specified month over the years
    suspend fun getRadiationDataForMonth(latitude: Double, longitude: Double, month: Int): Double{
        val radiationResults = datasource.getRadiationForLocation(latitude, longitude)
        return radiationResults.filter { it.month == month }.map { it.radiation }.average()
    }

    // Helper function to split the data into summer, winter and total
    private fun calculateAverage(items: List<MonthlyRadiation>): List<Double>{
        val summerMonths = listOf(4, 5, 6, 7, 8)
        val winterMonths = listOf(9, 10, 11, 12, 1, 2, 3)

        // Average radiation in the summer and winter
        val summerAverage = items.filter{ it.month in summerMonths }.map{ it.radiation }.average()
        val winterAverage = items.filter{ it.month in winterMonths }.map{ it.radiation }.average()

        // Regardless of period:
        val overallAverage = items.map { it.radiation }.average()

        return listOf(overallAverage, summerAverage, winterAverage)
    }
}
