package castle.core.behaviour.controller

import castle.core.builder.ProjectileBuilder
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.service.MapService

class GroundRangeUnitController(
    private val mapService: MapService,
    private val projectileBuilder: ProjectileBuilder
) {
    fun spawnProjectile(unitComponent: UnitComponent) {
        val unitPosition = PositionComponent.mapper.get(unitComponent.owner)
        val projectile = projectileBuilder.build()
        val projectilePosition = PositionComponent.mapper.get(projectile)
        projectilePosition.setMatrix(unitPosition)
    }
}