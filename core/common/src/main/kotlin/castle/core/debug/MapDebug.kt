package castle.core.debug

import castle.core.component.render.LineRenderComponent
import castle.core.component.render.SquareRenderComponent
import castle.core.path.Area
import castle.core.service.AreaService
import castle.core.service.EnvironmentService
import castle.core.service.MapScanService
import castle.core.util.ColorUtil
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single
import kotlin.math.abs

@Single
class MapDebug(
    private val engine: Engine,
    private val areaService: AreaService,
    private val mapScanService: MapScanService,
    private val environmentService: EnvironmentService
) {
    private var debugGridEnabled: Boolean = false
    private val debugGridEntity: MutableList<Entity> = ArrayList()
    private val debugAreaEntity: MutableList<MutableList<Entity>> = ArrayList()

    fun init() {
        for (i in 0 until mapScanService.width) {
            debugAreaEntity.add(ArrayList())
            for (j in 0 until mapScanService.height) {
                val area = areaService.setArea(Area(0, 0), i, j)
                val position = area.position
                val widthAll = abs(environmentService.mapAABBMax.x - environmentService.mapAABBMin.x)
                val heightAll = abs(environmentService.mapAABBMax.z - environmentService.mapAABBMin.z)
                val widthPerArea = widthAll / mapScanService.width
                val heightPerArea = heightAll / mapScanService.height
                val actualWidth = widthPerArea * 0.95f
                val actualHeight = heightPerArea * 0.95f
                val xOffset = (widthPerArea - actualWidth) / 2f
                val yOffset = (heightPerArea - actualHeight) / 2f
                val vector3 = Vector3(position.x + xOffset, 0f, position.y + yOffset)
                val squareRenderComponent = SquareRenderComponent(vector3, actualWidth, actualHeight)
                squareRenderComponent.hide = true
                val commonEntity = Entity().apply { add(squareRenderComponent) }
                engine.addEntity(commonEntity)
                debugAreaEntity[i].add(commonEntity)
            }
        }
    }

    fun clear() {
        debugAreaEntity.forEach { list ->
            list.forEach { entity ->
                val squareRenderComponent = SquareRenderComponent.mapper.get(entity)
                squareRenderComponent.hide = true
            }
        }
    }

    fun debugArea(area: Area) {
        if (!debugGridEnabled) return
        val squareRenderComponent = SquareRenderComponent.mapper.get(debugAreaEntity[area.x][area.y])
        squareRenderComponent.hide = false
    }

    fun switchDebugGrid() {
        debugGridEnabled = !debugGridEnabled

        if (!debugGridEnabled) {
            debugGridEntity.forEach { engine.removeEntity(it) }
            debugGridEntity.clear()

            debugAreaEntity.forEach { list ->
                list.forEach { entity ->
                    val squareRenderComponent = SquareRenderComponent.mapper.get(entity)
                    squareRenderComponent.hide = true
                }
            }
            return
        }

        val map2D = mapScanService.map
        map2D.forEachIndexed { i, _ ->
            val start = areaService.toArea(i, 0).position
            val end = areaService.toArea(i, mapScanService.map[i].size).position
            createLine(start, end)
        }
        map2D[0].forEachIndexed { j, _ ->
            val start = areaService.toArea(0, j).position
            val end = areaService.toArea(mapScanService.map.size, j).position
            createLine(start, end)
        }
    }

    private fun createLine(start: Vector2, end: Vector2) {
        val lineRenderComponent = LineRenderComponent(
            Vector3(start.x, 0f, start.y),
            Vector3(end.x, 0f, end.y),
            ColorUtil.dimmedColor(Color.RED, 0.7f),
            true
        )
        val commonEntity = Entity().apply { add(lineRenderComponent) }
        engine.addEntity(commonEntity)
        debugGridEntity.add(commonEntity)
    }
}