package com.akilas.solarsaverprojectakilas.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)

@Serializable
data class GeocodeResponse(
    val results: List<GeocodeResult>
)

@Serializable
data class GeocodeResult(
    val geometry: Geometry
)

@Serializable
data class Geometry(
    val location: Location
)