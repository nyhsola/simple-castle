package castle.server.ashley.systems.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3

class Line3DComponent : Component {
    var from: Vector3 = Vector3()
    var to: Vector3 = Vector3()
    var color: Color = Color.RED
    var show: Boolean = false

    companion object {
        val mapper: ComponentMapper<Line3DComponent> = ComponentMapper.getFor(Line3DComponent::class.java)
    }
}