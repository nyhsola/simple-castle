package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Array

class PositionComponent : Component {
    lateinit var matrix4: Matrix4
    lateinit var nodeName: String

    companion object {
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)

        fun createComponent(engine: Engine, constructor: Constructor): PositionComponent {
            val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java)
            positionComponent.matrix4 = constructor.getTransform()
            positionComponent.nodeName = constructor.nodesStr
            return positionComponent
        }

        fun getRootNode(nodes: String): String = if (getArr(nodes).size == 1) getArr(nodes)[0] else getArr(nodes)[1]

        fun getAllNodes(nodes: String) = getArr(nodes)

        private fun getArr(nodes: String) = Array(nodes.split(",").toTypedArray())
    }
}