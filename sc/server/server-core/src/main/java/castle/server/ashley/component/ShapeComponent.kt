package castle.server.ashley.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color

class ShapeComponent : Component {
    var width: Float = 0.0f
    var height: Float = 0.0f
    var color: Color = Color.WHITE

    companion object {
        val mapper: ComponentMapper<ShapeComponent> = ComponentMapper.getFor(ShapeComponent::class.java)

        fun createComponent(engine: Engine, width: Float, height: Float, color: Color = Color.WHITE): ShapeComponent {
            val shapeComponent: ShapeComponent = engine.createComponent(ShapeComponent::class.java)
            shapeComponent.width = width
            shapeComponent.height = height
            shapeComponent.color = color
            return shapeComponent
        }
    }
}