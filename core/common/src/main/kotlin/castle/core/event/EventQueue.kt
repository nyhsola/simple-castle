package castle.core.event

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import org.koin.core.annotation.Single
import java.util.*

@Single
class EventQueue : Listener<EventContext> {
    private val eventTypeQueue: PriorityQueue<EventContext> = PriorityQueue()
    private val temp: MutableSet<EventContext> = HashSet()

    fun proceed(operations: Map<String, (EventContext) -> Unit>) {
        if (eventTypeQueue.isNotEmpty()) {
            for (operation in operations) {
                for (event in eventTypeQueue) {
                    if (event.eventType == operation.key) {
                        operation.value.invoke(event)
                        temp.add(event)
                    }
                }
            }
            if (temp.isNotEmpty()) {
                eventTypeQueue.removeAll(temp)
                temp.clear()
            }
        }
    }

    override fun receive(signal: Signal<EventContext>, eventContext: EventContext) {
        eventTypeQueue.add(eventContext)
    }
}