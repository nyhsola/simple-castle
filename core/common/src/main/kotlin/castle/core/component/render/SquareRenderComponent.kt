package castle.core.component.render

import castle.core.util.ColorUtil
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class SquareRenderComponent(
    val vector3Offset: Vector3 = Vector3(),
    var width: Float = 0.0f,
    var height: Float = 0.0f,
    var matrix: Matrix4 = Matrix4(),
    var shapeType: ShapeType = ShapeType.Filled,
    var color: Color = ColorUtil.dimmedColor(Color.GREEN, 0.7f)
) : Component {
    companion object {
        val mapper: ComponentMapper<SquareRenderComponent> = ComponentMapper.getFor(SquareRenderComponent::class.java)
    }
}