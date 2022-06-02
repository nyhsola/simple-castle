package castle.core.component

import castle.core.state.StateDelta
import castle.core.state.StateMachineDelta
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity

class StateComponent(
    behaviour: StateDelta<Entity>,
    owner: Entity
) : Component {
    companion object {
        val mapper: ComponentMapper<StateComponent> = ComponentMapper.getFor(StateComponent::class.java)
    }

    val state: StateMachineDelta<Entity, StateDelta<Entity>> = StateMachineDelta(owner, behaviour)
}