package com.akilas.solarsaverprojectakilas.data.geocode.repository

import com.akilas.solarsaverprojectakilas.data.geocode.dataSource.GeocodeDataSource
import com.akilas.solarsaverprojectakilas.model.Location

class GeocodeRepository(private val dataSource: GeocodeDataSource) {
    suspend fun getLocation(address: String): Location? {
        val response = dataSource.getCoordinates(address)
        return response.results.firstOrNull()?.geometry?.location
    }
}