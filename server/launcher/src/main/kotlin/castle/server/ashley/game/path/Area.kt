package castle.server.ashley.game.path

import com.badlogic.gdx.math.Vector2

class Area(
    val position: Vector2,
    val x: Int,
    val y: Int
) {
    constructor(x: Int, y: Int) : this(Vector2(), x, y)

    var index: Int = 0

    fun isInRange(area: Area): Boolean {
        val radius = 1
        return (this.x == area.x && this.y == area.y)
                || (x + radius == area.x && y == area.y)
                || (x - radius == area.x && y == area.y)
                || (x == area.x && y + radius == area.y)
                || (x == area.x && y - radius == area.y)
                || (x - radius == area.x && y - radius == area.y)
                || (x == area.x - radius && y == area.y - radius)
                || (x - radius == area.x && y == area.y - radius)
                || (x == area.x - radius && y - radius == area.y)
    }

    fun getAreasInRange(): List<Area> {
        val radius = 1
        val list: MutableList<Area> = ArrayList()
        list.add(Area(x + radius, y))
        list.add(Area(x - radius, y))
        list.add(Area(x, y + radius))
        list.add(Area(x, y - radius))

        list.add(Area(x - radius, y - radius))
        list.add(Area(x + radius, y + radius))
        list.add(Area(x - radius, y + radius))
        list.add(Area(x + radius, y - radius))

        return list
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