package castle.server.ashley.physic

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody

class Constructor(private val model: Model,
                  val nodeName: String,
                  private val interactType: InteractType,
                  val mass: Float,
                  private val physicShape: PhysicShape,
                  val instantiate: Boolean,
                  val hide: Boolean) {

    fun getPhysicObject(): PhysicObject {
        val node = model.getNode(nodeName)
        val shape = physicShape.build(node)
        val info: btRigidBody.btRigidBodyConstructionInfo = btRigidBody.btRigidBodyConstructionInfo(mass, null, shape)
        return interactType.build(info)
    }

    fun getModel(): ModelInstance {
        return ModelInstance(model, nodeName, true)
    }

    fun getTransform(): Matrix4 {
        return ModelInstance(model, nodeName, true).transform
    }
}