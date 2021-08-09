package castle.core.game.event

data class EventContext(val eventType: EventType, val params: Map<String, Any> = emptyMap()) : Comparable<EventContext> {
    override fun compareTo(other: EventContext): Int {
        return eventType.compareTo(other.eventType)
    }
}