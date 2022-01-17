package castle.core.common.event

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import java.util.*

class EventQueue : Listener<EventContext> {
    private val eventTypeQueue: PriorityQueue<EventContext> = PriorityQueue()
    private val temp: ArrayList<EventContext> = ArrayList()

    fun proceed(operation: (EventContext) -> Boolean) {
        temp.clear()
        for (event in eventTypeQueue) {
            if (operation.invoke(event)) {
                temp.add(event)
            }
        }
        eventTypeQueue.removeAll(temp.toSet())
    }

    override fun receive(signal: Signal<EventContext>, eventContext: EventContext) {
        eventTypeQueue.add(eventContext)
    }
}