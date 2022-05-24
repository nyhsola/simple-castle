package castle.core.behaviour

import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity

class Behaviours(
    groundMeleeAttackBehaviour: GroundMeleeAttackBehaviour,
    groundRangeAttackBehaviour: GroundRangeAttackBehaviour,
    decorationBehaviour: DecorationBehaviour
) {
    val behaviors: Map<String, StateDelta<Entity>> = mapOf(
        "none" to NoneBehavior.INIT,
        "decoration" to decorationBehaviour.initState(),
        "ground-attack" to groundMeleeAttackBehaviour.initState(),
        "range-attack" to groundRangeAttackBehaviour.initState(),
    )
}