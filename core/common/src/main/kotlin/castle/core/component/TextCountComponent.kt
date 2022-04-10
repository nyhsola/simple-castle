package castle.core.component

import castle.core.event.EventContext
import castle.core.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.signals.Signal

class TextCountComponent(
        var count: Int,
        private val playerName: String,
        private val eventName: String,
        private val signal: Signal<EventContext>,
        private val params: Map<String, String> = mapOf(Pair(playerName, eventName)),
        val task: Task = object : Task(count.toFloat()) {
            override fun action() {
                signal.dispatch(EventContext(eventName, params))
                reset()
            }
        }
) : Component {
    fun left(): Float {
        return count - task.accumulate
    }

    companion object {
        val mapper: ComponentMapper<TextCountComponent> = ComponentMapper.getFor(TextCountComponent::class.java)
    }
}