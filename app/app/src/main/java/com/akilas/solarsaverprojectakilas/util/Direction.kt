
package no.uio.ifi.in2000.team14.solarsaver.ui.utils

enum class Direction(private val retning: String): Name {

    NORTH("Nord"),
    SOUTH("Sør"),
    EAST("Øst"),
    WEST("Vest");

    override fun fetchName() = this.retning
}
