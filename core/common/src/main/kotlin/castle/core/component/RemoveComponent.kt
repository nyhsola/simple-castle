package castle.core.component

import castle.core.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper

class RemoveComponent(
        val removeInSec: Float = 1f
) : Component {
    companion object {
        val mapper: ComponentMapper<RemoveComponent> = ComponentMapper.getFor(RemoveComponent::class.java)
    }

    val task: Task = object : Task(removeInSec) {
        override fun action() {
            isDone = true
        }
    }

    var isDone = false
}