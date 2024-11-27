package castle.core.service

import castle.core.component.MapComponent
import castle.core.debug.MapDebug
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.path.Area
import castle.core.path.AreaGraph
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class MapService(
    private val eventQueue: EventQueue,
    private val areaService: AreaService,
    private val mapScanService: MapScanService,
    private val mapDebug: MapDebug
) {
    companion object {
        const val DEBUG_GRID = "DEBUG_GRID"
    }

    private val mapGraph = AreaGraph()
    private val tempArr = HashSet<Entity>()

    val unitsInArea: MutableMap<Area, MutableSet<Entity>> = HashMap()
    private val areasInUnit: MutableMap<Entity, MutableSet<Area>> = HashMap()

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(DEBUG_GRID) {  mapDebug.switchDebugGrid(unitsInArea.filter { it.value.isNotEmpty() }.keys) }
    )

    fun init() {
        initializeGraph(mapScanService.map)
    }

    fun update() {
        eventQueue.proceed(operations)
    }

    fun updateEntity(entity: Entity) {
        removeFromMap(entity)
        addOnMap(entity)
    }

    fun getNear(area: Area, radius: Float): Collection<Entity> {
        tempArr.clear()
        area.forEachInRadius(radius) { x, y -> unitsInArea[Area(x, y)]?.let { tempArr.addAll(it) } }
        return tempArr
    }

    fun getPath(list: List<Vector3>): GraphPath<Area> {
        val areas = list.map {
            val area = Area(0, 0)
            areaService.setArea(area, it)
        }
        val path = DefaultGraphPath<Area>()
        for (i in 0 until areas.size - 2) {
            mapGraph.findPath(areas[i], areas[i + 1]).forEach { path.add(it) }
        }
        return path
    }

    fun inRadius(currentArea: Area, nextArea: Area): Boolean {
        var inRadius = false
        currentArea.forEachInRadius(3f) { x, y -> inRadius = inRadius || (nextArea.x == x && nextArea.y == y) }
        return inRadius
    }

    fun removeFromMap(entity: Entity) {
        areasInUnit[entity]?.forEach { mapGraph.restore(it) }
        areasInUnit[entity]?.forEach { unitsInArea[it]?.remove(entity) }
        areasInUnit.remove(entity)
    }

    private fun addOnMap(entity: Entity) {
        val mapComponent = MapComponent.mapper.get(entity)
        mapComponent.fitAreas.forEach {
            mapGraph.disconnect(it)
            unitsInArea.getOrPut(it) { mutableSetOf() }.add(entity)
            areasInUnit.getOrPut(entity) { mutableSetOf() }.add(it)
        }
    }

    private fun initializeGraph(map2D: List<List<Int>>) {
        for (i in map2D.indices) {
            for (j in map2D[i].indices) {
                mapGraph.addArea(areaService.toArea(i, j))
            }
        }
        map2D.forEachIndexed { i, it1 ->
            it1.forEachIndexed { j, it2 ->
                if (it2 == 0) {
                    val area = Area(i, j)
                    if (i - 1 >= 0 && map2D[i - 1][j] == 0) {
                        mapGraph.connect(area, Area(i - 1, j))
                    }
                    if (i + 1 < map2D.size && map2D[i + 1][j] == 0) {
                        mapGraph.connect(area, Area(i + 1, j))
                    }
                    if (j - 1 >= 0 && map2D[i][j - 1] == 0) {
                        mapGraph.connect(area, Area(i, j - 1))
                    }
                    if (j + 1 < map2D[i].size && map2D[i][j + 1] == 0) {
                        mapGraph.connect(area, Area(i, j + 1))
                    }
                }
            }
        }
    }
}