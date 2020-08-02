package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Matrix4

class PositionComponent : Component {
    lateinit var matrix4: Matrix4

    companion object {
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)

        fun createComponent(engine: PooledEngine, constructor: Constructor): PositionComponent {
            val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java)
            positionComponent.matrix4 = constructor.getTransform()
            return positionComponent
        }
    }
}