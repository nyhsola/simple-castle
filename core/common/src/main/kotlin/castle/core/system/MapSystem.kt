package castle.core.system

import castle.core.component.MapComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.service.AreaService
import castle.core.service.MapService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class MapSystem(
    private val areaService: AreaService,
    private val mapService: MapService
) : IteratingSystem(family), EntityListener {
    companion object {
        private val family: Family = Family.all(MapComponent::class.java).get()
    }

    private val temp: Vector3 = Vector3()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val mapComponent = MapComponent.mapper.get(entity)
        val position = PositionComponent.mapper.get(entity).matrix4.getTranslation(temp)
        areaService.setArea(mapComponent.currentArea, position)
        mapService.updateEntity(entity)
    }

    override fun entityRemoved(entity: Entity) {
        mapService.removeFromMap(entity)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val mapComponent = MapComponent.mapper.get(entity)
        if (mapComponent.isMovable) {
            val position = PositionComponent.mapper.get(entity).matrix4.getTranslation(temp)
            areaService.setArea(mapComponent.currentArea, position)
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
}