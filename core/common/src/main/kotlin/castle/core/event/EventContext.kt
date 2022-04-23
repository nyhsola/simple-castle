package castle.core.event

data class EventContext(val eventType: String, val params: Map<String, Any> = emptyMap()) : Comparable<EventContext> {
    override fun compareTo(other: EventContext): Int {
        return eventType.compareTo(other.eventType)
    }
}