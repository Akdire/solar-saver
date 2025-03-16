package com.akilas.solarsaverprojectakilas.util

object SolarCalculator {
    // Calculate yearly kWh from radiation and weather
    fun calculateKwh(
        radiation: Double, // Avg kWh/m²
        temp: Double,     // °C
        clouds: Double,   // %
        snow: Double,     // cm
        panelArea: Double = 10.0, // m²
        efficiency: Double = 0.2  // 20%
    ): Double {
        val tempFactor = if (temp > 25) 0.95 else if (temp < -5) 0.98 else 1.0
        val cloudFactor = 1 - (clouds / 100) * 0.5
        val snowFactor = if (snow > 0) 0.5 else 1.0
        return radiation * panelArea * efficiency * tempFactor * cloudFactor * snowFactor * 12 // Yearly
    }
}