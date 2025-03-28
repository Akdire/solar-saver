
package com.akilas.solarsaverprojectakilas.util

import android.util.Log

fun calculateNetRadiation(
    radiation: Double,
    temperature: Double,
    cloudiness: Double,
    snowLevel: Double
): Double{

    // Very simplified calculations for MVP purposes

    val optimalTempMin = -10.0
    val optimalTempMax = 25.0
    val temperatureAdjustment = when {
        temperature < optimalTempMin || temperature > optimalTempMax -> 0.9
        else -> 1.0
    }

    val cloudAdjustment = 1 - (cloudiness/8 * 0.05)
    val snowAdjustment = 1 - (snowLevel/4 * 0.25)

    Log.d("cloudAdj", cloudAdjustment.toString())
    Log.d("snowAdj", snowAdjustment.toString())
    Log.d("Temperaturverdi", temperature.toString())
    Log.d("Skyverdi", cloudiness.toString())
    Log.d("Snøverdi", snowLevel.toString())

    return radiation*temperatureAdjustment*cloudAdjustment*snowAdjustment
}
