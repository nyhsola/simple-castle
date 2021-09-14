package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.model.Animation
import com.badlogic.gdx.graphics.g3d.utils.AnimationController

class AnimationComponent(private val renderComponent: RenderComponent) : Component {
    val animationController: AnimationController = AnimationController(renderComponent.modelInstance)

    fun setAnimation(animation: String, speed: Float) {
        val animationFull = renderComponent.armature + "|" + animation
        animationController.paused = false
        animationController.setAnimation(animationFull, -1, speed, animationListener)
    }

    companion object {
        val mapper: ComponentMapper<AnimationComponent> = ComponentMapper.getFor(AnimationComponent::class.java)
        private val animationListener = object : AnimationController.AnimationListener {
            override fun onEnd(animation: AnimationController.AnimationDesc) {
            }

            override fun onLoop(animation: AnimationController.AnimationDesc) {
            }
        }
    }
}