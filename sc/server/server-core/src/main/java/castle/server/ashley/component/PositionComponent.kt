package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import java.util.*

class PositionComponent : Component {
    lateinit var matrix4: Matrix4
    lateinit var nodeName: String

    companion object {
        val temp: Vector3 = Vector3()
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)

        fun createComponent(engine: Engine, constructor: Constructor): PositionComponent {
            val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java)
            positionComponent.matrix4 = constructor.getTransform()
            positionComponent.nodeName = constructor.node
            return positionComponent
        }

        fun createComponent(engine: Engine, matrix4: Matrix4): PositionComponent {
            val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java)
            positionComponent.matrix4 = matrix4
            positionComponent.nodeName = "generated-${UUID.randomUUID()}"
            return positionComponent
        }
    }


    fun setPositionFrom(from: Matrix4) {
        from.getTranslation(temp)
        matrix4.setTranslation(temp.x, temp.y, temp.z)
    }
}