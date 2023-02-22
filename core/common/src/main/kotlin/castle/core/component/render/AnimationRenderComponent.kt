package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.utils.AnimationController
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener

class AnimationRenderComponent(private val modelComponent: ModelRenderComponent) : Component {
    val animationController: AnimationController = AnimationController(modelComponent.modelInstance)

    fun setAnimation(animation: String, totalDuration: Float) {
        animationController.paused = false
        animationController.setAnimation(
            modelComponent.formatAnimation(animation),
            -1,
            modelComponent.getAnimation(animation).duration / totalDuration,
            animationListener
        )
    }

    fun setAnimation(animation: String, totalDuration: Float, animationListener: AnimationListener) {
        animationController.paused = false
        animationController.setAnimation(
            modelComponent.formatAnimation(animation),
            -1,
            modelComponent.getAnimation(animation).duration / totalDuration,
            animationListener
        )
    }

    companion object {
        val mapper: ComponentMapper<AnimationRenderComponent> = ComponentMapper.getFor(AnimationRenderComponent::class.java)
        private val animationListener = object : AnimationListener {
            override fun onEnd(animation: AnimationController.AnimationDesc) {
            }

            override fun onLoop(animation: AnimationController.AnimationDesc) {
            }
        }
    }
}