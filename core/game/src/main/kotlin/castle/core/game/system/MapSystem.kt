package castle.core.game.system

import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.common.system.IteratingIntervalSystem
import castle.core.game.ui.game.Minimap
import castle.core.game.service.MapService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family

class MapSystem(
    private val mapService: MapService,
    private val minimap: Minimap
) : IteratingIntervalSystem(MAP_TICK, family) {
    private companion object {
        private val family: Family = Family.all(PositionComponent::class.java, RenderComponent::class.java).get()
        private const val MAP_TICK: Float = 1f / 10f
    }

    override fun tickUpdate(deltaTime: Float) {
        mapService.clear()
        super.tickUpdate(deltaTime)
        minimap.update(mapService.objectsOnMap)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = RenderComponent.mapper.get(entity)
        if (!renderComponent.hideOnMap) {
            mapService.add(entity)
        }
    }
}