package castle.core.game.component

import castle.core.game.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity

class AttackComponent(
    val attackAmount: IntRange,
    val attackSpeed: Float,
    val scanRange: Float,
    var target: Entity? = null,
    var enableAttacking: Boolean = false
) : Component {
    val attackTask: Task = object : Task(attackSpeed) {
        override fun action() {
            val hpComponent = HPComponent.mapper.get(target)
            hpComponent.takeDamage(attackAmount.random())
        }
    }
    var nearObjects: List<Entity> = ArrayList()

    companion object {
        val mapper: ComponentMapper<AttackComponent> = ComponentMapper.getFor(AttackComponent::class.java)
    }
}