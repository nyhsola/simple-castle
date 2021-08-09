package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

class RectComponent : Component {
    var width: Float = 0.0f
    var height: Float = 0.0f
    var color: Color = Color.WHITE
    var position: Vector2 = Vector2()

    companion object {
        val mapper: ComponentMapper<RectComponent> = ComponentMapper.getFor(RectComponent::class.java)
    }
}