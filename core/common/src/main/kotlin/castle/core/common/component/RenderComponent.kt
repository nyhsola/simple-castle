package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.ModelInstance

class RenderComponent(
    val modelInstance: ModelInstance,
    var hide: Boolean,
    val nodeName: String,
    val armature: String
) : Component {
    companion object {
        val mapper: ComponentMapper<RenderComponent> = ComponentMapper.getFor(RenderComponent::class.java)
    }
}