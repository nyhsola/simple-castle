package castle.core.game.util

abstract class Task(private val interval: Float) {
    private var accumulate: Float = 0.0f

    fun update(delta: Float) {
        accumulate += delta
        while (accumulate >= interval) {
            accumulate -= interval
            action()
        }
    }

    abstract fun action()
}