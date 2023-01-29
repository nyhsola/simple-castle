package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.model.Animation

class ModelRenderComponent(
        val modelInstance: ModelInstance,
        var hide: Boolean,
        val nodeName: String
) : Component {
    companion object {
        val mapper: ComponentMapper<ModelRenderComponent> = ComponentMapper.getFor(ModelRenderComponent::class.java)
    }

    fun getAnimation(animation: String) : Animation {
        return modelInstance.animations.first { formatAnimation(animation) == it.id }
    }

    fun formatAnimation(animation: String) = "$nodeName|$animation"
}