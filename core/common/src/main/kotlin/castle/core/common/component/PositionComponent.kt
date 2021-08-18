package castle.core.common.component

import castle.core.common.json.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Matrix4

class PositionComponent(val constructor: Constructor) : Component {
    val matrix4 = constructor.getMatrix4()

    companion object {
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)
    }
}