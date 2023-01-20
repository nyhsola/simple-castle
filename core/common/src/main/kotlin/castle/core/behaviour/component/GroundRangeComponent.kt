package castle.core.behaviour.component

import castle.core.component.UnitComponent
import castle.core.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper

class GroundRangeComponent(
        val attackAmount: IntRange,
        val attackSpeed: Float
) : Component {
    companion object {
        val mapper: ComponentMapper<GroundRangeComponent> = ComponentMapper.getFor(GroundRangeComponent::class.java)
    }

    var isDone = false
    var targetEnemy: UnitComponent? = null

    val attackTask: Task = object : Task(attackSpeed) {
        override fun action() {
            targetEnemy?.takeDamage(attackAmount.random())
            isDone = true
        }
    }
}