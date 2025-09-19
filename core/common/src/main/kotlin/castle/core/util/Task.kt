package castle.core.util

abstract class Task(private val interval: Float) {
    var accumulate: Float = 0.0f
    var disabled: Boolean = false

    fun update(delta: Float) {
        if (disabled) return
        accumulate += delta
        while (accumulate >= interval) {
            accumulate -= interval
            action()
        }
    }

    fun reset() {
        accumulate = 0.0f
    }

    abstract fun action()
}