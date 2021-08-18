package castle.core.common.json

import castle.core.common.physic.PhysicInstance
import castle.core.common.physic.PhysicShape
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array

class Constructor(private val model: Model, constructorJson: ConstructorJson) {
    private var collisionFlag: List<String> = constructorJson.collisionFlag
    private val physicShape: PhysicShape = constructorJson.shape
    private var collisionFilterMask: List<Int> = constructorJson.collisionFilterMask
    private val mass: Float = constructorJson.mass
    val node: String = constructorJson.nodes
    val armature: String = constructorJson.armature
    var collisionFilterGroup: Int = constructorJson.collisionFilterGroup
    val instantiate: Boolean = constructorJson.instantiate
    var hide: Boolean = constructorJson.hide

    fun getPhysicInstance(): PhysicInstance {
        val shape = physicShape.build(getModel())
        val info: btRigidBody.btRigidBodyConstructionInfo = btRigidBody.btRigidBodyConstructionInfo(mass, null, shape)
        return PhysicInstance(info, collisionFlag, collisionFilterGroup, collisionFilterMask)
    }

    fun getModelInstance(): ModelInstance {
        return getModel().apply {
            transform.mul(model.getNode(node).localTransform)
            nodes.forEach { node ->
                node.translation.set(0f, 0f, 0f)
                node.scale.set(1f, 1f, 1f)
                calculateTransforms()
            }
        }
    }

    fun getMatrix4(): Matrix4 {
        return model.getNode(node).globalTransform.cpy()
    }

    private fun getModel(): ModelInstance {
        val array = if (armature.isNotEmpty()) listOf(armature, node).toTypedArray() else listOf(node).toTypedArray()
        return ModelInstance(model, Array(array))
    }
}