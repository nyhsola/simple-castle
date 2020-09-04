package castle.server.ashley.systems

import castle.server.ashley.component.AnimationComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family

class AnimationSystem : IteratingSystemAdapter(Family.all(AnimationComponent::class.java, RenderComponent::class.java).get()), EntityListener {

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

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        AnimationComponent.mapper.get(entity).animationController.update(deltaTime)
    }
}