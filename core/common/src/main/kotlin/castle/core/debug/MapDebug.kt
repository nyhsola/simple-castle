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
    private val debugAreaEntity: MutableList<Entity> = ArrayList()

    fun switchDebugGrid(areas: Set<Area>) {
        debugGridEnabled = !debugGridEnabled

        if (!debugGridEnabled) {
            debugAreaEntity.forEach { engine.removeEntity(it) }
            debugAreaEntity.clear()

            debugGridEntity.forEach { engine.removeEntity(it) }
            debugGridEntity.clear()
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

        val widthAll = abs(environmentService.mapAABBMax.x - environmentService.mapAABBMin.x)
        val heightAll = abs(environmentService.mapAABBMax.z - environmentService.mapAABBMin.z)
        areas.forEach {
            val position = it.position
            val squareRenderComponent = SquareRenderComponent(
                Vector3(position.x, 0.3f, position.y),
                widthAll / mapScanService.width,
                heightAll / mapScanService.height
            )
            val commonEntity = Entity().apply { add(squareRenderComponent) }
            engine.addEntity(commonEntity)
            debugAreaEntity.add(commonEntity)
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