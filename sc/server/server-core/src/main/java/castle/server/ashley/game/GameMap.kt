package castle.server.ashley.game

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
        val boxScan = Vector3(1.7f, 1.7f, 1.7f)
    }

    private val from = Vector3()
    private val to = Vector3()

    private val map = run {
        val regionMin = Vector3()
        val regionMax = Vector3()
        val physicObject = resourceManager.constructorMap["ground"]?.getPhysicInstance()
        physicObject?.body?.getAabb(regionMin, regionMax)
        physicObject?.dispose()
        from.set(regionMin)
        to.set(regionMax)
        mapService.scanRegion(boxScan, regionMin, regionMax)
    }

    val flatMap = map.map { xMap -> xMap.map { zMap -> zMap.sum() } }

    private val areaGraph = run {
        val graph = AreaGraph()

        flatMap.forEachIndexed { i, it1 ->
            it1.forEachIndexed { j, _ ->
                val area = Area(i, j)
                graph.addArea(area)
            }
        }

        flatMap.forEachIndexed { i, it1 ->
            it1.forEachIndexed { j, it2 ->
                if (it2 == 0) {
                    val area = graph.getArea(i, j)

                    if (i - 1 >= 0 && flatMap[i - 1][j] == 0) {
                        graph.connect(area, graph.getArea(i - 1, j))
                    }

                    if (i + 1 < flatMap.size && flatMap[i + 1][j] == 0) {
                        graph.connect(area, graph.getArea(i + 1, j))
                    }

                    if (j - 1 >= 0 && flatMap[i][j - 1] == 0) {
                        graph.connect(area, graph.getArea(i, j - 1))
                    }

                    if (j + 1 < flatMap[i].size && flatMap[i][j + 1] == 0) {
                        graph.connect(area, graph.getArea(i, j + 1))
                    }
                }
            }
        }
        graph
    }

    fun getPath(from: Vector3, to: Vector3): GraphPath<Area> {
        return areaGraph.findPath(toArea(from), toArea(to))
    }

    fun toFlatPosition(area: Area): Vector2 {
        val width = boxScan.x * 2
        val depth = boxScan.z * 2

        return Vector2(to.x - area.x * width, to.z - area.y * depth)
    }

    fun isInRangeOfArea(position: Vector3, area: Area): Boolean {
        val positionArea = toArea(position)
        if (positionArea == area) return true
        if (areaGraph.getAreaOrNull(positionArea.x + 1, positionArea.y) == area) return true
        if (areaGraph.getAreaOrNull(positionArea.x - 1, positionArea.y) == area) return true
        if (areaGraph.getAreaOrNull(positionArea.x, positionArea.y + 1) == area) return true
        if (areaGraph.getAreaOrNull(positionArea.x, positionArea.y - 1) == area) return true
        return false
    }

    private fun toArea(position: Vector3): Area {
        val width = boxScan.x * 2
        val depth = boxScan.z * 2

        val x = (abs(from.x + position.x) / width).toInt()
        val z = (abs(from.z + position.z) / depth).toInt()

        return areaGraph.getArea(x, z)
    }

}