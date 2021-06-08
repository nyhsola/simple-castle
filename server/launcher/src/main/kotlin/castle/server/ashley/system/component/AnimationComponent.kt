package castle.server.ashley.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.utils.AnimationController

class AnimationComponent : Component {
    lateinit var animationController: AnimationController
    lateinit var animate: String

    companion object {
        val mapper: ComponentMapper<AnimationComponent> = ComponentMapper.getFor(AnimationComponent::class.java)

        fun postConstruct(renderComponent: RenderComponent, animationComponent: AnimationComponent) {
            animationComponent.animationController = AnimationController(renderComponent.modelInstance)
            animationComponent.animationController.animate(animationComponent.animate, 1f)
            animationComponent.animationController.setAnimation(animationComponent.animate, -1)
        }
    }
}