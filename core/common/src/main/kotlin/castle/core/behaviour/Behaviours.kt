package castle.core.behaviour

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.State

class Behaviours {
    companion object {
        val behaviors: Map<String, State<Entity>> = mapOf(
                "none" to NoneBehavior.INIT,
                "ground-attack" to GroundAttackBehaviour.INIT,
                "range-attack" to RangeAttackBehaviour.INIT,
        )
    }
}