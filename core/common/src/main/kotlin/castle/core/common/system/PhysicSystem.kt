package castle.core.common.system

import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.common.service.PhysicService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.utils.Disposable

class PhysicSystem(
    private val physicService: PhysicService
) : IteratingSystem(family), EntityListener, Disposable {
    private companion object {
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
        physicService.renderDebug()
        physicService.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {}

    override fun dispose() {
        physicService.dispose()
    }
}