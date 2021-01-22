package castle.server.ashley.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color

class ShapeComponent : Component {
    var width: Float = 0.0f
    var height: Float = 0.0f
    var color: Color = Color.WHITE

    companion object {
        val mapper: ComponentMapper<ShapeComponent> = ComponentMapper.getFor(ShapeComponent::class.java)
    }
}