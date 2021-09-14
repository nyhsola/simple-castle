package castle.core.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine

class BehaviourComponent(
    owner: Entity,
    initialState: State<Entity>
) : Component {
    val state: StateMachine<Entity, State<Entity>> = DefaultStateMachine(owner, initialState)

    companion object {
        val mapper: ComponentMapper<BehaviourComponent> = ComponentMapper.getFor(BehaviourComponent::class.java)
    }
}