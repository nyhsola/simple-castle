package castle.core.common.system

import castle.core.common.component.AnimationComponent
import castle.core.common.component.RenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class AnimationSystem : IteratingSystem(Family.all(AnimationComponent::class.java, RenderComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        AnimationComponent.mapper.get(entity).animationController.update(deltaTime)
    }
}