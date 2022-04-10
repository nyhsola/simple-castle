package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.utils.AnimationController

class AnimationRenderComponent(private val modelRenderComponent: ModelRenderComponent) : Component {
    val animationController: AnimationController = AnimationController(modelRenderComponent.modelInstance)

    fun setAnimation(animation: String, speed: Float) {
        animationController.paused = false
        animationController.setAnimation("${modelRenderComponent.nodeName}|$animation", -1, speed, animationListener)
    }

    companion object {
        val mapper: ComponentMapper<AnimationRenderComponent> = ComponentMapper.getFor(AnimationRenderComponent::class.java)
        private val animationListener = object : AnimationController.AnimationListener {
            override fun onEnd(animation: AnimationController.AnimationDesc) {
            }

            override fun onLoop(animation: AnimationController.AnimationDesc) {
            }
        }
    }
}