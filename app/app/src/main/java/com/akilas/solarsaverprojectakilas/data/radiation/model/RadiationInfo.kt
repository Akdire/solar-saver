package com.akilas.solarsaverprojectakilas.data.radiation.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Classes to serialize the radiation-data
@Serializable
data class RadiationOutputs(
    val outputs: Outputs
)

@Serializable
data class Outputs(
    val monthly: List<MonthlyRadiation>
)

@Serializable
data class MonthlyRadiation(
    val year: Int,
    val month: Int,
    @SerialName("H(h)_m") val radiation: Double
)