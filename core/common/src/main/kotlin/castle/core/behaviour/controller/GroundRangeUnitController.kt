package castle.core.behaviour.controller

import castle.core.behaviour.component.GroundRangeComponent
import castle.core.builder.ProjectileBuilder
import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class GroundRangeUnitController(
        private val engine: Engine,
        private val projectileBuilder: ProjectileBuilder
) {
    private val force = 200f
    private val tempVectorFrom = Vector3()
    private val tempVectorTo = Vector3()

    fun init(entity: Entity) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val unitJson = unitComponent.unitJson
        val meleeComponent = GroundRangeComponent(
                unitJson.attackFrom..unitJson.attackTo,
                unitJson.attackSpeed
        )
        entity.add(meleeComponent)
    }

    fun throwProjectile(fromUnitComponent: UnitComponent, toUnitComponent: UnitComponent) {
        val fromPosition = PositionComponent.mapper.get(fromUnitComponent.owner)
        val toPosition = PositionComponent.mapper.get(toUnitComponent.owner)
        val projectile = projectileBuilder.build()
        val projectilePosition = PositionComponent.mapper.get(projectile)
        projectilePosition.setMatrix(fromPosition)
        val fromV = fromPosition.matrix4.getTranslation(tempVectorFrom)
        val toV = toPosition.matrix4.getTranslation(tempVectorTo)
        val direction = toV.sub(fromV).nor().scl(force)
        val physicComponent = PhysicComponent.mapper.get(projectile)
        physicComponent.body.applyCentralForce(direction)
        engine.addEntity(projectile)
    }
}