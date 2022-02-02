package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class Circle3DComponent(
    val vector3Offset: Vector3 = Vector3(),
    var matrix4Track: Matrix4 = Matrix4(),
    var radius: Float = 0.0f
) : Component {
    companion object {
        val mapper: ComponentMapper<Circle3DComponent> = ComponentMapper.getFor(Circle3DComponent::class.java)
    }
}