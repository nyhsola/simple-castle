package castle.server.ashley.physic

import castle.server.ashley.component.PositionComponent
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody

class Constructor(private val model: Model,
                  val nodesStr: String,
                  private val interactType: InteractType,
                  val mass: Float,
                  private val physicShape: PhysicShape,
                  val instantiate: Boolean,
                  val hide: Boolean,
                  var animation: String) {

    fun getPhysicObject(): PhysicObject {
        val shape = physicShape.build(getModel())
        val info: btRigidBody.btRigidBodyConstructionInfo = btRigidBody.btRigidBodyConstructionInfo(mass, null, shape)
        return interactType.build(info)
    }

    fun getRenderModel(): ModelInstance {
        return getModel().apply {
            transform.mul(model.getNode(PositionComponent.getRootNode(nodesStr)).localTransform)
            nodes.forEach { node ->
                node.translation.set(0f, 0f, 0f)
                node.scale.set(1f, 1f, 1f)
                calculateTransforms()
            }
        }
    }

    fun getTransform(): Matrix4 {
        return model.getNode(PositionComponent.getRootNode(nodesStr)).globalTransform
    }

    private fun getModel(): ModelInstance {
        return ModelInstance(model, PositionComponent.getAllNodes(nodesStr))
    }

}