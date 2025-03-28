
package com.akilas.solarsaverprojectakilas.util

enum class Direction(private val retning: String): Name {

    NORTH("Nord"),
    SOUTH("Sør"),
    EAST("Øst"),
    WEST("Vest");

    override fun fetchName() = this.retning
}
