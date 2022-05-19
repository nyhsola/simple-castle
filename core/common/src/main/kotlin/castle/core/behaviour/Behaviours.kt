package castle.core.behaviour

import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity

class Behaviours(
    groundMeleeAttackBehaviour: GroundMeleeAttackBehaviour,
    groundRangeAttackBehaviour: GroundRangeAttackBehaviour
) {
    val behaviors: Map<String, StateDelta<Entity>> = mapOf(
        "none" to NoneBehavior.INIT,
        "ground-attack" to groundMeleeAttackBehaviour.initState(),
        "range-attack" to groundRangeAttackBehaviour.initState(),
    )
}