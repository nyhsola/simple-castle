package castle.core.system

import castle.core.component.MapComponent
import castle.core.component.UnitComponent
import castle.core.service.MapService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import org.koin.core.annotation.Single

@Single
class MapSystem(
    private val mapService: MapService
) : IteratingSystem(family) {
    companion object {
        private val family: Family = Family.all(MapComponent::class.java).get()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val mapComponent = MapComponent.mapper.get(entity)

        if (mapComponent.isStatic && !mapComponent.once) {
            mapService.updateEntity(entity)
            mapComponent.once = true
        }

        if (!mapComponent.isStatic) {
            mapService.updateEntity(entity)
        }

        if (UnitComponent.mapper.has(entity)) {
            val unitComponent = UnitComponent.mapper.get(entity)
            mapComponent.inRadiusEntities.clear()
            if (mapComponent.shouldSearchEntities) {
                mapComponent.inRadiusEntities.addAll(mapService.getNear(unitComponent.currentArea, unitComponent.visibilityRange))
            }
            if (unitComponent.isDead) {
                mapService.removeFromMap(entity)
            }
        }
    }
}