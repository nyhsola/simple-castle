package castle.core.util

import java.lang.Float.min

abstract class Task(private val interval: Float) {
    var accumulate: Float = 0.0f

    fun update(delta: Float) {
        accumulate += min(delta, 0.25f)
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