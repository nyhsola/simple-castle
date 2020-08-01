package castle.server.ashley.physic

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.model.Node
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array

class Constructor(private val model: Model,
                  val nodes: String,
                  private val interactType: InteractType,
                  val mass: Float,
                  private val physicShape: PhysicShape,
                  val instantiate: Boolean,
                  val hide: Boolean,
                  var animation: String) {

    fun getPhysicObject(): PhysicObject {
        val node = model.getNode(getRootNode(nodes))
        val shape = physicShape.build(node)
        val info: btRigidBody.btRigidBodyConstructionInfo = btRigidBody.btRigidBodyConstructionInfo(mass, null, shape)
        return interactType.build(info)
    }

    fun getModel(): ModelInstance {
        val modelInstance = ModelInstance(model, getAllNodes(nodes))

        modelInstance.nodes.forEach { node: Node ->
            node.translation.set(0f, 0f, 0f)
            node.rotation.idt()
            node.scale.set(1f, 1f, 1f)
        }
        modelInstance.calculateTransforms()

        return modelInstance
    }

    fun getTransform(): Matrix4 {
        return model.getNode(getRootNode(nodes)).globalTransform
    }

    private fun getRootNode(nodes: String): String = if (getArr(nodes).size > 1) getArr(nodes)[1] else getArr(nodes)[0]

    private fun getAllNodes(nodes: String) = getArr(nodes)

    private fun getArr(nodes: String) = Array(nodes.split(",").toTypedArray())
}