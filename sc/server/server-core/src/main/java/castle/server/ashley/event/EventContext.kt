package castle.server.ashley.event

data class EventContext(val eventType: EventType, val params: Map<String, Any> = emptyMap()) : Comparable<EventType> {
    override fun compareTo(other: EventType): Int {
        return this.compareTo(other)
    }
}