package castle.core.event

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import java.util.*

class EventQueue : Listener<EventContext> {
    private val eventTypeQueue: PriorityQueue<EventContext> = PriorityQueue()
    private val temp: MutableSet<EventContext> = HashSet()

    fun proceed(operation: (EventContext) -> Boolean) {
        if (eventTypeQueue.isNotEmpty()) {
            temp.clear()
            for (event in eventTypeQueue) {
                if (operation.invoke(event)) {
                    temp.add(event)
                }
            }
            eventTypeQueue.removeAll(temp)
        }
    }

    override fun receive(signal: Signal<EventContext>, eventContext: EventContext) {
        eventTypeQueue.add(eventContext)
    }
}