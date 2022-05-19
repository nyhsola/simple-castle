package castle.core.path

import com.badlogic.gdx.math.Vector2
import kotlin.math.pow

class Area(
    val position: Vector2,
    val x: Int,
    val y: Int
) {
    constructor(x: Int, y: Int) : this(Vector2(), x, y)

    var index: Int = 0

    fun inRadius(radius: Float, area: Area): Boolean {
        var isInArea = false
        forEachInRadius(radius) { x, y -> isInArea = (x == area.x && y == area.y) || isInArea }
        return isInArea
    }

    fun forEachInRadius(radius: Float, action: (Int, Int) -> Unit) {
        val radius2 = radius.pow(2)
        val startX = (x - radius).toInt()
        val endX = (x + radius).toInt()
        val startY = (y - radius).toInt()
        val endY = (y + radius).toInt()
        for (xi in startX until endX) {
            for (yi in startY until endY) {
                val xi2 = (xi - x).toFloat().pow(2)
                val yi2 = (yi - y).toFloat().pow(2)
                if (xi2 + yi2 < radius2) {
                    action.invoke(xi, yi)
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}