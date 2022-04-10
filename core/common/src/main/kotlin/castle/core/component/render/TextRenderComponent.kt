package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector3

class TextRenderComponent(
        var text: String,
        val offset: Vector3
) : Component {
    companion object {
        val mapper: ComponentMapper<TextRenderComponent> = ComponentMapper.getFor(TextRenderComponent::class.java)
    }
}