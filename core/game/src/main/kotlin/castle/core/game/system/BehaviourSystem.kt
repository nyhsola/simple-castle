package castle.core.game.system

import castle.core.common.system.IteratingIntervalSystem
import castle.core.game.component.BehaviourComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family

class BehaviourSystem : IteratingIntervalSystem(BEHAVIOUR_TICK, family) {
    private companion object {
        private val family: Family = Family.all(BehaviourComponent::class.java).get()
        private const val BEHAVIOUR_TICK: Float = 1f / 30f
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        BehaviourComponent.mapper.get(entity).state.update()
    }
}