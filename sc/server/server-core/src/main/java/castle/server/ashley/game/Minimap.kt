package castle.server.ashley.game

import castle.server.ashley.path.Area
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Minimap(engine: Engine, intMap: Array<IntArray>) {
    companion object {
        const val miniMapWidth = 300f
        const val miniMapHeight = 300f

        const val startPositionX = 1600f
        const val startPositionY = 25f
    }

    private val areas: MutableList<MutableList<Area>> = ArrayList()

    init {
        val boxCountWidth = intMap.size
        val boxCountHeight = intMap[0].size

        val pointWidth = miniMapWidth / boxCountWidth
        val pointHeight = miniMapHeight / boxCountHeight

        val list = Array(boxCountWidth) { i ->
            Array(boxCountHeight) { j ->
                val area = Area(engine)
                area.setBox(Vector2(pointWidth, pointHeight))
                area.setPosition(Vector2(i.toFloat(), j.toFloat()), Vector3(startPositionX, startPositionY, 0f))
                area.setOccupied(intMap[i][j])
                area
            }
        }.map { it.toMutableList() }.toMutableList()

        areas.addAll(list)
    }
}