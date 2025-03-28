
package com.akilas.solarsaverprojectakilas.util

import android.util.Log

fun calculatePowerProduction(
    radiation: Double,
    roofArea: Double,
    roofDegrees: Double,
    roofDirection: String
): Double {

    // Very simplified for MVP purposes

    val directionAdjustment = when (roofDirection.uppercase()) {
        "NORD" -> 0.7
        "ØST" -> 0.9
        "VEST" -> 0.9
        "SØR" -> 1.0
        else -> 1.0
    }

    Log.d("Direction", roofDirection)
    Log.d("DirectionAdjusted", directionAdjustment.toString())

    val angleOptimal = 15.0..40.0
    val angleAdjustment = if (roofDegrees in angleOptimal) 1.0 else 0.9

    return (radiation * roofArea * directionAdjustment * angleAdjustment) * 0.2175
}
