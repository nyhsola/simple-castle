package castle.core.system.render

import castle.core.component.render.AnimationRenderComponent
import castle.core.component.render.ModelRenderComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class AnimationRenderSystem : IteratingSystem(Family.all(AnimationRenderComponent::class.java, ModelRenderComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        AnimationRenderComponent.mapper.get(entity).animationController.update(deltaTime)
    }
}