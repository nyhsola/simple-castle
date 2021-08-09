package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Matrix4

class PositionComponent : Component {
    lateinit var matrix4: Matrix4

    companion object {
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)
    }
}