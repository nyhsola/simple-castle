package castle.core.common.event

data class EventContext(val eventType: String, val params: Map<String, Any> = emptyMap()) : Comparable<EventContext> {
    override fun compareTo(other: EventContext): Int {
        return eventType.compareTo(other.eventType)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventContext

        if (eventType != other.eventType) return false
        if (params != other.params) return false

        return true
    }

    override fun hashCode(): Int {
        var result = eventType.hashCode()
        result = 31 * result + params.hashCode()
        return result
    }
}