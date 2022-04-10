package castle.core.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

abstract class IteratingIntervalSystem(private val interval: Float, family: Family) : IteratingSystem(family) {
    private var accumulator = 0f

    override fun update(deltaTime: Float) {
        accumulator += deltaTime
        while (accumulator >= interval) {
            accumulator -= interval
            tickUpdate(deltaTime)
        }
    }

    open fun tickUpdate(deltaTime: Float) {
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {}
}