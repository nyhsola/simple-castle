package castle.core.system

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.event.EventQueue
import castle.core.service.PhysicService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.utils.Disposable

class PhysicSystem(
    private val eventQueue: EventQueue,
    private val physicService: PhysicService
) : IteratingSystem(family), EntityListener, Disposable {
    companion object {
        const val DEBUG_ENABLE = "PHYSIC_ENABLE"
        private val family: Family = Family.all(PositionComponent::class.java, PhysicComponent::class.java).get()
    }

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityRemoved(entity: Entity) {
        physicService.removeEntity(entity)
    }

    override fun entityAdded(entity: Entity) {
        physicService.addEntity(entity)
    }

    override fun update(deltaTime: Float) {
        eventQueue.proceed {
            when (it.eventType) {
                DEBUG_ENABLE -> {
                    physicService.debugEnabled = !physicService.debugEnabled
                    true
                }
                else -> false
            }
        }
        physicService.renderDebug()
        physicService.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {}

    override fun dispose() {
        physicService.dispose()
    }
}