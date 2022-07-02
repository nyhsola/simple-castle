package castle.core.event

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import org.koin.core.annotation.Single
import java.util.*

@Single
class EventQueue : Listener<EventContext> {
    private val eventTypeQueue: PriorityQueue<EventContext> = PriorityQueue()
    private val temp: MutableSet<EventContext> = HashSet()

    fun proceed(map: Map<String, (EventContext) -> Unit>) {
        if (eventTypeQueue.isNotEmpty()) {
            temp.clear()
            for (entry in map) {
                proceed { eventContext ->
                    val function = map[eventContext.eventType]
                    function?.invoke(eventContext)
                    function != null
                }
            }
            eventTypeQueue.removeAll(temp)
        }
    }

    private fun proceed(operation: (EventContext) -> Boolean) {
        for (event in eventTypeQueue) {
            if (operation.invoke(event)) {
                temp.add(event)
            }
        }
    }

    override fun receive(signal: Signal<EventContext>, eventContext: EventContext) {
        eventTypeQueue.add(eventContext)
    }
}