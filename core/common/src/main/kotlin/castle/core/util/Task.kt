package castle.core.util

abstract class Task(private val interval: Float) {
    var accumulate: Float = 0.0f

    fun update(delta: Float) {
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