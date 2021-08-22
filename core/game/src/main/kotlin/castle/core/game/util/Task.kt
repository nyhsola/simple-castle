package castle.core.game.util

abstract class Task(private val interval: Float) {
    private var accumulate: Float = 0.0f
    private var isStarted = false

    fun update(delta: Float) {
        if (isStarted) {
            accumulate += delta
            while (accumulate >= interval) {
                accumulate -= interval
                action()
            }
        }
    }

    fun start() {
        isStarted = true
    }

    fun stop() {
        isStarted = false
    }

    abstract fun action()
}