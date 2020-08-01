package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g3d.utils.AnimationController

class AnimationComponent : Component {
    lateinit var animationController: AnimationController
    var animate: String = ""

    companion object {
        val mapper: ComponentMapper<AnimationComponent> = ComponentMapper.getFor(AnimationComponent::class.java)

        fun createComponent(engine: Engine, constructor: Constructor): AnimationComponent {
            val animationComponent: AnimationComponent = engine.createComponent(AnimationComponent::class.java)
            animationComponent.animate = constructor.animation
            return animationComponent
        }

        fun postConstruct(renderComponent: RenderComponent, animationComponent: AnimationComponent) {
            animationComponent.animationController = AnimationController(renderComponent.modelInstance)
            animationComponent.animationController.animate(animationComponent.animate, 1f)
            animationComponent.animationController.setAnimation(animationComponent.animate, -1)
        }
    }
}