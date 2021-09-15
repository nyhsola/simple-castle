package castle.core.game.service

import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.game.path.Area
import castle.core.game.path.AreaGraph
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class MapService(
    private val scanService: ScanService
) {
    private val mapGraph by lazy { initializeGraph(scanService.map) }
    private val tempVector = Vector3()
    private val tempArr = ArrayList<Area>()
    private val aabbMin = Vector3()
    private val aabbMax = Vector3()

    val objectsOnMap: MutableMap<Area, MutableList<Entity>> = HashMap()

    fun clear() {
        objectsOnMap.clear()
    }

    fun add(entity: Entity) {
        PositionComponent.mapper.get(entity).matrix4.getTranslation(tempVector)
        PhysicComponent.mapper.get(entity).physicInstance.body.getAabb(aabbMin, aabbMax)
        val min = scanService.toArea(aabbMin)
        val max = scanService.toArea(aabbMax)
        if (min.x - max.x <= 1) {
            objectsOnMap.getOrPut(scanService.toArea(tempVector)) { mutableListOf() }.add(entity)
        } else {
            for (i in max.x until min.x) {
                for (j in max.y until min.y) {
                    objectsOnMap.getOrPut(Area(i, j)) { mutableListOf() }.add(entity)
                }
            }
        }
    }

    fun getNearObjects(radius: Float, area: Area): List<Entity> {
        tempArr.clear()
        val areasInRange = area.getAreasInRange(radius, tempArr)
        return areasInRange.mapNotNull { objectsOnMap[it] }.flatten()
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
        tempArr.clear()
        return scanService.toArea(position).getAreasInRange(2f, tempArr).any { it == area }
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