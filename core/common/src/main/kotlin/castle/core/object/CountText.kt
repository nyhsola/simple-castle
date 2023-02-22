package castle.core.`object`

import castle.core.component.render.TextRenderComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.util.Task
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.math.Vector3

class CountText(
    position: Vector3,
    lineNumber: Int,
    eventQueue: EventQueue
) : Entity() {
    companion object {
        const val ON_COUNT = "ON_COUNT"
        const val PARAM_LINE = "LINE"
    }

    private val textRenderComponent = TextRenderComponent("", position).also { this.add(it) }
    private val count: Int = (25..30).random()
    private val signal: Signal<EventContext> = Signal()
    private val task: Task =
        object : Task(count.toFloat()) {
            override fun action() {
                signal.dispatch(EventContext(ON_COUNT, mapOf(Pair(PARAM_LINE, lineNumber))))
                reset()
            }
        }

    init {
        signal.add(eventQueue)
    }

    fun update(deltaTime: Float) {
        textRenderComponent.text = "%.0f".format(left())
        task.update(deltaTime)
    }

    fun left(): Float {
        return count - task.accumulate
    }
}