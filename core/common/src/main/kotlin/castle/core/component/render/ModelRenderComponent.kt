package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.ModelInstance

class ModelRenderComponent(
        val modelInstance: ModelInstance,
        var hide: Boolean,
        val nodeName: String
) : Component {
    companion object {
        val mapper: ComponentMapper<ModelRenderComponent> = ComponentMapper.getFor(ModelRenderComponent::class.java)
    }
}