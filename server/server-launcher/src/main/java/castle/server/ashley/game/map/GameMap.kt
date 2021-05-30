package castle.server.ashley.game.map

import castle.server.ashley.game.path.Area
import castle.server.ashley.game.path.AreaGraph
import castle.server.ashley.service.MapService
import castle.server.ashley.utils.ResourceManager
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.math.abs

class GameMap(mapService: MapService, resourceManager: ResourceManager) {
    companion object {
        private val SCAN_BOX = Vector3(1.7f, 1.7f, 1.7f)
    }

    private val aabbMin = Vector3()
    private val aabbMax = Vector3()

    private val map3D = run {
        resourceManager.constructorMap["ground"]!!.getPhysicInstance()
            .apply { body.getAabb(aabbMin, aabbMax) }
            .also { it.dispose() }
            .let { mapService.scanRegion(SCAN_BOX, aabbMin, aabbMax) }
    }

    val map2D = map3D.map { byX -> byX.map { byZ -> byZ.sum() } }

    private val mapGraph = AreaGraph().also {
        for (i in map2D.indices) {
            for (j in map2D[i].indices) {
                it.addArea(Area(i, j))
            }
        }

        map2D.forEachIndexed { i, it1 ->
            it1.forEachIndexed { j, it2 ->
                if (it2 == 0) {
                    val area = it.getArea(i, j)
                    if (i - 1 >= 0 && map2D[i - 1][j] == 0) {
                        it.connect(area, it.getArea(i - 1, j))
                    }
                    if (i + 1 < map2D.size && map2D[i + 1][j] == 0) {
                        it.connect(area, it.getArea(i + 1, j))
                    }
                    if (j - 1 >= 0 && map2D[i][j - 1] == 0) {
                        it.connect(area, it.getArea(i, j - 1))
                    }
                    if (j + 1 < map2D[i].size && map2D[i][j + 1] == 0) {
                        it.connect(area, it.getArea(i, j + 1))
                    }
                }
            }
        }
    }

    fun getPath(from: Vector3, to: Vector3): GraphPath<Area> {
        return mapGraph.findPath(toArea(from), toArea(to))
    }

    fun toFlatPosition(area: Area): Vector2 {
        val width = SCAN_BOX.x * 2
        val depth = SCAN_BOX.z * 2

        return Vector2(aabbMax.x - area.x * width, aabbMax.z - area.y * depth)
    }

    fun isInRangeOfArea(position: Vector3, area: Area): Boolean {
        val positionArea = toArea(position)
        if (positionArea == area) return true
        if (mapGraph.getAreaOrNull(positionArea.x + 1, positionArea.y) == area) return true
        if (mapGraph.getAreaOrNull(positionArea.x - 1, positionArea.y) == area) return true
        if (mapGraph.getAreaOrNull(positionArea.x, positionArea.y + 1) == area) return true
        if (mapGraph.getAreaOrNull(positionArea.x, positionArea.y - 1) == area) return true
        return false
    }

    private fun toArea(position: Vector3): Area {
        val width = SCAN_BOX.x * 2
        val depth = SCAN_BOX.z * 2

        val x = (abs(aabbMin.x + position.x) / width).toInt()
        val z = (abs(aabbMin.z + position.z) / depth).toInt()

        return mapGraph.getArea(x, z)
    }

}