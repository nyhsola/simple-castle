package castle.core.system

import castle.core.component.StateComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager

class StateSystem : IteratingSystem(family) {
    companion object {
        private val family: Family = Family.all(StateComponent::class.java).get()
    }

    override fun update(deltaTime: Float) {
        GdxAI.getTimepiece().update(deltaTime)
        MessageManager.getInstance().update()
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        StateComponent.mapper.get(entity).state.update(deltaTime)
    }
}