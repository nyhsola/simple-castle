package castle.server.ashley.systems

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PlayerComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.UnitComponent
import castle.server.ashley.physic.Constructor
import castle.server.ashley.service.ConstructorManagerService
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Vector3

class PlayerSystem(private val constructorManagerService: ConstructorManagerService) : IteratingSystemAdapter(Family.all(PlayerComponent::class.java).get()) {

    companion object {
        val angularDefaultFactor: Vector3 = Vector3(0.0f, 1.0f, 0.0f)
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val playerComponent = PlayerComponent.mapper.get(entity)
        playerComponent.accumulate += deltaTime
        while (playerComponent.accumulate >= playerComponent.spawnRate) {
            playerComponent.accumulate -= playerComponent.spawnRate
            createUnits(playerComponent)
        }
    }

    private fun createUnits(playerComponent: PlayerComponent) {
        playerComponent.paths.forEach { path ->
            val unit = constructorManagerService.constructorMap.getValue(playerComponent.unitType).instantiate(engine)
            val spawn = constructorManagerService.constructorMap.getValue(path.component1())
            val area = constructorManagerService.constructorMap.getValue(path.component2())
            createUnit(unit, spawn, area)
        }
    }

    private fun createUnit(entity: Entity, spawn: Constructor, aim: Constructor) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)

        positionComponent.setPositionFrom(spawn.getTransform())
        physicComponent.setAngularFactor(angularDefaultFactor)

        val createComponent = UnitComponent.createComponent(engine, aim)

        entity.add(createComponent)
        engine.addEntity(entity)
    }
}