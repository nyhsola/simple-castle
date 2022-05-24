package castle.core.system

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.LineRenderComponent
import castle.core.event.EventQueue
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3

class UnitSystem(
    private val eventQueue: EventQueue
) : IteratingSystem(family) {
    companion object {
        const val DEBUG_ENABLE = "DEBUG_PATH_ENABLE"
        private val family: Family = Family.all(UnitComponent::class.java, PositionComponent::class.java, PhysicComponent::class.java).get()
    }

    private var debugEnabled: Boolean = false
    private val lines: MutableList<Entity> = ArrayList()
    private val tempPosition: Vector3 = Vector3()

    override fun update(deltaTime: Float) {
        proceedEvents()
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (UnitComponent.mapper.get(entity).isDead) {
            engine.removeEntity(entity)
        }
    }

    private fun proceedEvents() {
        eventQueue.proceed { eventContext ->
            when (eventContext.eventType) {
                DEBUG_ENABLE -> {
                    debugEnabled = !debugEnabled
                    if (debugEnabled) {
                        engine.getEntitiesFor(family).onEach { createDebugLines(it, lines) }
                        lines.onEach { engine.addEntity(it) }
                    } else {
                        lines.onEach { engine.removeEntity(it) }
                        lines.clear()
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun createDebugLines(entity: Entity, linesOut: MutableList<Entity>) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val positionComponent = PositionComponent.mapper.get(entity)
        for (j in 0 until unitComponent.mainPath.count - 2) {
            val area1 = unitComponent.mainPath.get(j).position
            val area2 = unitComponent.mainPath.get(j + 1).position
            val unitPosition = positionComponent.matrix4.getTranslation(tempPosition)
            val lineRenderComponent = LineRenderComponent(
                Vector3(area1.x, unitPosition.y, area1.y), Vector3(area2.x, unitPosition.y, area2.y), Color.GREEN, true
            )
            val lineEntity = Entity()
            lineEntity.add(lineRenderComponent)
            linesOut.add(lineEntity)
        }
    }
}