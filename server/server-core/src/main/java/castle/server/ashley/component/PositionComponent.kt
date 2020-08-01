package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Matrix4

class PositionComponent : Component {
    lateinit var matrix4: Matrix4
    lateinit var nodeName: String

    companion object {
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)

        fun createComponent(engine: Engine, constructor: Constructor): PositionComponent {
            val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java)
            positionComponent.matrix4 = constructor.getTransform()
            positionComponent.nodeName = constructor.nodes
            return positionComponent
        }
    }
}