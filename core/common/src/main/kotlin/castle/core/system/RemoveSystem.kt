package castle.core.system

import castle.core.component.RemoveComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import org.koin.core.annotation.Single

@Single
class RemoveSystem : IteratingSystem(family) {
    companion object {
        private val family: Family = Family.all(RemoveComponent::class.java).get()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeComponent = RemoveComponent.mapper.get(entity)
        removeComponent.task.update(deltaTime)
        if (removeComponent.isDone) {
            engine.removeEntity(entity)
        }
    }
}