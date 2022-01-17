package castle.core.game.system

import castle.core.common.system.IteratingIntervalSystem
import castle.core.game.component.AttackComponent
import castle.core.game.component.PathComponent
import castle.core.game.service.MapService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family

class AttackSystem(
    private val mapService: MapService
) : IteratingIntervalSystem(ATTACK_TICK, family) {
    private companion object {
        private val family: Family = Family.all(AttackComponent::class.java).get()
        private const val ATTACK_TICK: Float = 0.1f
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val attackComponent = AttackComponent.mapper.get(entity)
        val pathComponent = PathComponent.mapper.get(entity)
        val graph = pathComponent.graphPath
        val nextPosition = pathComponent.nextPosition
        val nextArea = graph[nextPosition]
        attackComponent.nearObjects = mapService.getNearObjects(attackComponent.range, nextArea)
        if (attackComponent.enableAttacking) {
            attackComponent.attackTask.update(deltaTime)
        }
    }
}