package castle.core.common.system

import castle.core.common.component.AnimationComponent
import castle.core.common.component.RenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class AnimationSystem : IteratingSystem(Family.all(AnimationComponent::class.java, RenderComponent::class.java).get()), EntityListener {
    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(Family.all(AnimationComponent::class.java, RenderComponent::class.java).get(), this)
        super.addedToEngine(engine)
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun entityAdded(entity: Entity) {
        val renderComponent = RenderComponent.mapper.get(entity)
        val animationComponent = AnimationComponent.mapper.get(entity)
        AnimationComponent.postConstruct(renderComponent, animationComponent)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        AnimationComponent.mapper.get(entity).animationController.update(deltaTime)
    }
}