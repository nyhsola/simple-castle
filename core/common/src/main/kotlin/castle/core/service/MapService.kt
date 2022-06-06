package castle.core.service

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.render.CircleRenderComponent
import castle.core.event.EventQueue
import castle.core.path.Area
import castle.core.path.AreaGraph
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.math.abs

class MapService(
    private val engine: Engine,
    private val eventQueue: EventQueue,
    private val mapScanService: MapScanService,
    private val environmentService: EnvironmentService
) {
    companion object {
        const val DEBUG_ENABLE = "DEBUG_MAP_ENABLE"
    }

    private var debugEnabled: Boolean = false
    private val circles: MutableList<Entity> = ArrayList()

    private val mapGraph = AreaGraph()
    private val tempVector = Vector3()
    private val tempArr = HashSet<Entity>()
    private val aabbMin = Vector3()
    private val aabbMax = Vector3()

    val unitsInArea: MutableMap<Area, MutableSet<Entity>> = HashMap()
    private val areasInUnit: MutableMap<Entity, MutableSet<Area>> = HashMap()

    fun init() {
        initializeGraph(mapScanService.map)
    }

    fun update() {
        proceedEvents(engine)
    }

    fun updateEntity(entity: Entity) {
        removeFromMap(entity)
        placeOnMap(entity)
    }

    fun removeFromMap(entity: Entity) {
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
        val areas = list.map { toArea(it) }
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

    fun toArea(position: Vector3): Area {
        val width = MapScanService.scanBox.x * 2
        val depth = MapScanService.scanBox.z * 2
        val posX = abs(environmentService.aabbMin.x + position.x)
        val poxZ = abs(environmentService.aabbMin.z + position.z)
        val x = posX.div(width).toInt()
        val y = poxZ.div(depth).toInt()
        return toArea(x, y)
    }

    private fun toArea(i: Int, j: Int): Area {
        val x = environmentService.aabbMax.x - i * MapScanService.scanBox.x * 2 - MapScanService.scanBox.x
        val z = environmentService.aabbMax.z - j * MapScanService.scanBox.z * 2 - MapScanService.scanBox.z
        return Area(Vector2(x, z), i, j)
    }

    private fun proceedEvents(engine: Engine) {
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
        val min = toArea(aabbMin)
        val max = toArea(aabbMax)
        val isBiggerThanOneGrid = abs(min.x - max.x) > 1
        if (!isBiggerThanOneGrid) {
            val area = toArea(PositionComponent.mapper.get(entity).matrix4.getTranslation(tempVector))
            placeOnMapInternal(area, entity)
        } else {
            for (i in max.x until min.x + 1) {
                for (j in max.y until min.y + 1) {
                    val area = toArea(i, j)
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

    private fun initializeGraph(map2D: List<List<Int>>) {
        for (i in map2D.indices) {
            for (j in map2D[i].indices) {
                mapGraph.addArea(toArea(i, j))
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

    private fun createDebugCircles(circlesOut: MutableList<Entity>) {
        unitsInArea
            .filter { it.value.isNotEmpty() }
            .keys
            .forEach {
                val commonEntity = Entity()
                val circleRenderComponent = CircleRenderComponent()
                val position = it.position
                circleRenderComponent.vector3Offset.set(position.x, 0.3f, position.y)
                circleRenderComponent.radius = 1f
                circleRenderComponent.shapeType = ShapeRenderer.ShapeType.Filled
                commonEntity.add(circleRenderComponent)
                circlesOut.add(commonEntity)
            }
    }
}