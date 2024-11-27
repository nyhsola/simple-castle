package castle.core.system

import castle.core.component.MapComponent
import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.path.Area
import castle.core.service.AreaService
import castle.core.service.MapService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single
import kotlin.math.abs

@Single
class MapSystem(
    private val areaService: AreaService,
    private val mapService: MapService
) : IteratingSystem(family), EntityListener {
    companion object {
        private val family: Family = Family.all(MapComponent::class.java).get()
    }

    private val temp: Vector3 = Vector3()
    private val tempAABBMin = Vector3()
    private val tempAABBMax = Vector3()
    private val tempArea1 = Area(0, 0)
    private val tempArea2 = Area(0, 0)
    private val tempAreas = ArrayList<Area>()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val mapComponent = MapComponent.mapper.get(entity)
        updatePosition(entity, mapComponent)
        mapService.updateEntity(entity)
    }

    override fun entityRemoved(entity: Entity) {
        mapService.removeFromMap(entity)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val mapComponent = MapComponent.mapper.get(entity)
        if (mapComponent.isMovable) {
            updatePosition(entity, mapComponent)
            mapService.updateEntity(entity)
        }
        if (UnitComponent.mapper.has(entity)) {
            val unitComponent = UnitComponent.mapper.get(entity)
            mapComponent.inRadiusEntities.clear()
            if (mapComponent.shouldSearchEntities) {
                mapComponent.inRadiusEntities.addAll(mapService.getNear(mapComponent.currentArea, unitComponent.visibilityRange))
            }
        }
    }

    private fun updatePosition(entity: Entity, mapComponent: MapComponent) {
        val position = PositionComponent.mapper.get(entity).matrix4.getTranslation(temp)
        areaService.setArea(mapComponent.currentArea, position)

        PhysicComponent.mapper.get(entity).body.getAabb(tempAABBMin, tempAABBMax)

        val min = areaService.setArea(tempArea1, tempAABBMin)
        val max = areaService.setArea(tempArea2, tempAABBMax)

        tempAreas.addAll(mapComponent.fitAreas)
        mapComponent.fitAreas.clear()

        val isBiggerThanOneGrid = abs(min.x - max.x) > 1
        if (isBiggerThanOneGrid) {
            for (i in max.x until min.x + 1) {
                for (j in max.y until min.y + 1) {
                    val area = tempAreas.removeLastOrNull() ?: Area(0, 0)
                    areaService.setArea(area, i, j)
                    mapComponent.fitAreas.add(area)
                }
            }
        } else {
            mapComponent.fitAreas.add(mapComponent.currentArea)
        }

        tempAreas.clear()
    }
}