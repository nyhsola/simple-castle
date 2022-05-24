package castle.core.service

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.render.CircleRenderComponent
import castle.core.event.EventQueue
import castle.core.path.Area
import castle.core.path.AreaGraph
import castle.core.ui.game.Minimap
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import kotlin.math.abs

class MapService(
    private val eventQueue: EventQueue,
    private val minimap: Minimap,
    private val scanService: ScanService
) {
    companion object {
        const val DEBUG_ENABLE = "DEBUG_MAP_ENABLE"
    }

    private var debugEnabled: Boolean = false
    private val circles: MutableList<Entity> = ArrayList()

    private val mapGraph by lazy { initializeGraph(scanService.map) }
    private val tempVector = Vector3()
    private val tempArr = HashSet<Entity>()
    private val aabbMin = Vector3()
    private val aabbMax = Vector3()

    private val unitsInArea: MutableMap<Area, MutableSet<Entity>> = HashMap()
    private val areasInUnit: MutableMap<Entity, MutableSet<Area>> = HashMap()

    fun updateMap() {
        minimap.update(unitsInArea)
    }

    fun updateEntity(entity: Entity) {
        removeEntity(entity)
        placeOnMap(entity)
    }

    fun removeEntity(entity: Entity) {
        areasInUnit[entity]?.forEach { mapGraph.restore(it) }
        areasInUnit[entity]?.forEach { unitsInArea[it]?.remove(entity) }
        areasInUnit.remove(entity)
    }

    fun getNear(area: Area, radius: Float): Collection<Entity> {
        tempArr.clear()
        area.forEachInRadius(radius) { x, y -> unitsInArea[Area(x, y)]?.let { tempArr.addAll(it) } }
        return tempArr
    }

    fun getPath(list: List<Vector3>): GraphPath<Area> {
        val areas = list.map { scanService.toArea(it) }
        val path = DefaultGraphPath<Area>()
        for (i in 0 until areas.size - 2) {
            mapGraph.findPath(areas[i], areas[i + 1]).forEach { path.add(it) }
        }
        return path
    }

    fun inRadius(position: Vector3, area: Area): Boolean {
        return scanService.toArea(position) == area
    }

    fun proceedEvents(engine: Engine) {
        eventQueue.proceed { eventContext ->
            when (eventContext.eventType) {
                DEBUG_ENABLE -> {
                    debugEnabled = !debugEnabled
                    if (debugEnabled) {
                        createDebugCircles(circles)
                        circles.forEach { engine.addEntity(it) }
                    } else {
                        circles.forEach { engine.removeEntity(it) }
                        circles.clear()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun placeOnMap(entity: Entity) {
        PhysicComponent.mapper.get(entity).body.getAabb(aabbMin, aabbMax)
        val min = scanService.toArea(aabbMin)
        val max = scanService.toArea(aabbMax)
        val isBiggerThanOneGrid = abs(min.x - max.x) > 1
        if (!isBiggerThanOneGrid) {
            val area = scanService.toArea(PositionComponent.mapper.get(entity).matrix4.getTranslation(tempVector))
            placeOnMapInternal(area, entity)
        } else {
            for (i in max.x until min.x + 1) {
                for (j in max.y until min.y + 1) {
                    val area = scanService.toArea(i, j)
                    placeOnMapInternal(area, entity)
                }
            }
        }
    }

    private fun placeOnMapInternal(area: Area, entity: Entity) {
        mapGraph.disconnect(area)
        unitsInArea.getOrPut(area) { mutableSetOf() }.add(entity)
        areasInUnit.getOrPut(entity) { mutableSetOf() }.add(area)
    }

    private fun initializeGraph(map2D: List<List<Int>>): AreaGraph {
        val areaGraph = AreaGraph()
        for (i in map2D.indices) {
            for (j in map2D[i].indices) {
                areaGraph.addArea(scanService.toArea(i, j))
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

    private fun createDebugCircles(circlesOut: MutableList<Entity>) {
        unitsInArea
            .filter { it.value.isNotEmpty() }
            .keys
            .forEach {
                val commonEntity = Entity()
                val circleRenderComponent = CircleRenderComponent()
                val position = it.position
                circleRenderComponent.vector3Offset.set(position.x, 0f, position.y)
                circleRenderComponent.radius = 1f
                circleRenderComponent.shapeType = ShapeRenderer.ShapeType.Filled
                commonEntity.add(circleRenderComponent)
                circlesOut.add(commonEntity)
            }
    }
}