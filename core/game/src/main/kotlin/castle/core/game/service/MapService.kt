package castle.core.game.service

import castle.core.game.`object`.Players
import castle.core.game.`object`.unit.GameObject
import castle.core.game.path.Area
import castle.core.game.path.AreaGraph
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class MapService(
    private val scanService: ScanService
) {
    private val mapGraph = initializeGraph(scanService.map)
    val objectsOnMap: MutableMap<Area, GameObject> = HashMap()

    fun update(players: Players) {
        players.getUnits()
            .also { objectsOnMap.clear() }
            .associateBy { scanService.toArea(it.unitPosition) }
            .filter { it.key.x >= 0 && it.key.y >= 0 }
            .forEach { objectsOnMap[it.key] = it.value }
    }

    fun getNearObjects(position: Vector3): List<GameObject> {
        val areasInRange = scanService.toArea(position).getAreasInRange()
        return areasInRange.mapNotNull { objectsOnMap[it] }
    }

    fun getPath(list: List<Vector3>): GraphPath<Area> {
        val areas = list.map { scanService.toArea(it) }
        val path = DefaultGraphPath<Area>()
        for (i in 0 until areas.size - 2) {
            mapGraph.findPath(areas[i], areas[i + 1]).forEach { path.add(it) }
        }
        return path
    }

    fun withinArea(position: Vector3, area: Area): Boolean {
        return scanService.toArea(position).isInRange(area)
    }

    private fun initializeGraph(map2D: List<List<Int>>): AreaGraph {
        val areaGraph = AreaGraph()
        for (i in map2D.indices) {
            for (j in map2D[i].indices) {
                val x = ScanService.aabbMax.x - i * ScanService.SCAN_BOX.x * 2
                val z = ScanService.aabbMax.z - j * ScanService.SCAN_BOX.z * 2
                val position = Vector2(x, z)
                areaGraph.addArea(Area(position, i, j))
            }
        }
        map2D.forEachIndexed { i, it1 ->
            it1.forEachIndexed { j, it2 ->
                if (it2 == 0) {
                    val area = Area(i, j)
                    if (i - 1 >= 0 && map2D[i - 1][j] == 0) {
                        areaGraph.connect(area, Area(i - 1, j))
                    }
                    if (i + 1 < map2D.size && map2D[i + 1][j] == 0) {
                        areaGraph.connect(area, Area(i + 1, j))
                    }
                    if (j - 1 >= 0 && map2D[i][j - 1] == 0) {
                        areaGraph.connect(area, Area(i, j - 1))
                    }
                    if (j + 1 < map2D[i].size && map2D[i][j + 1] == 0) {
                        areaGraph.connect(area, Area(i, j + 1))
                    }
                }
            }
        }
        return areaGraph
    }
}