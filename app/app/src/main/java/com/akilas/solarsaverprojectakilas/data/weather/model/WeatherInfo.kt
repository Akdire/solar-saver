package com.akilas.solarsaverprojectakilas.data.weather.model

import kotlinx.serialization.Serializable

// These two classes are used to serialize the location-data (sources)
@Serializable
data class MetaDataLocation(
    val data: List<LocationInfo>
)

@Serializable
data class LocationInfo(
    val id: String
)

// These three classes are used to serialize the observation-data
@Serializable
data class OutputObservation(
    val data: List<ObservationMetaData>
)

@Serializable
data class ObservationMetaData(
    val observations: List<ObservationInfo>
)

@Serializable
data class ObservationInfo(
    val elementId: String,
    val value: Double
)


