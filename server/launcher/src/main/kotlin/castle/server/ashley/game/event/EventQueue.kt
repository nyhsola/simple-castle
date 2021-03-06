package castle.server.ashley.game.event

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import java.util.*

class EventQueue : Listener<EventContext> {
    private val eventTypeQueue: PriorityQueue<EventContext> = PriorityQueue()

    fun pollAll(): Array<EventContext> {
        val gameEvents = eventTypeQueue.toTypedArray()
        eventTypeQueue.clear()
        return gameEvents
    }

    override fun receive(signal: Signal<EventContext>, eventContext: EventContext) {
        eventTypeQueue.add(eventContext)
    }
}