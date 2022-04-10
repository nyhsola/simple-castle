package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3

class LineRenderComponent(
        var from: Vector3 = Vector3(),
        var to: Vector3 = Vector3(),
        var color: Color = Color.GREEN,
        var show: Boolean = false
) : Component {
    companion object {
        val mapper: ComponentMapper<LineRenderComponent> = ComponentMapper.getFor(LineRenderComponent::class.java)
    }
}