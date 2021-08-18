package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.utils.AnimationController

class AnimationComponent(renderComponent: RenderComponent) : Component {
    val animationController: AnimationController = AnimationController(renderComponent.modelInstance)

    companion object {
        val mapper: ComponentMapper<AnimationComponent> = ComponentMapper.getFor(AnimationComponent::class.java)
    }
}