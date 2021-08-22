package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Rect3DComponent : Component {
    var width: Float = 0.0f
    var height: Float = 0.0f
    var color: Color = Color.GREEN
    var offset: Vector3 = Vector3()
    var matrix4: Matrix4 = Matrix4()

    companion object {
        val mapper: ComponentMapper<Rect3DComponent> = ComponentMapper.getFor(Rect3DComponent::class.java)

        fun postConstruct(positionComponent: PositionComponent, rect3DComponent: Rect3DComponent) {
            rect3DComponent.matrix4 = positionComponent.matrix4
        }
    }
}