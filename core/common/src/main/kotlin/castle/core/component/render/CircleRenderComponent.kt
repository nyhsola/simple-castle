package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class CircleRenderComponent(
        val vector3Offset: Vector3 = Vector3(),
        var matrix4Track: Matrix4 = Matrix4(),
        var radius: Float = 0.0f,
        var shapeType: ShapeType = ShapeType.Line,
        var color: Color = Color.GREEN
) : Component {
    companion object {
        val mapper: ComponentMapper<CircleRenderComponent> = ComponentMapper.getFor(CircleRenderComponent::class.java)
    }
}