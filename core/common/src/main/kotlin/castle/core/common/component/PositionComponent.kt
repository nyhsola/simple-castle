package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class PositionComponent(
    val matrix4: Matrix4
) : Component {
    // TODO: 2/2/2022 OffsetComponent
    var matrix4Track: Matrix4? = null
    val vector3Offset: Vector3 = Vector3()
    companion object {
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)
    }
}