package castle.core.game.utils

import castle.core.game.utils.json.SceneObjectJson
import castle.core.physic.PhysicInstance
import castle.core.physic.PhysicShape
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array

class Constructor(private val model: Model, sceneObjectJson: SceneObjectJson) {
    private var collisionFlag: List<String> = sceneObjectJson.collisionFlag
    private val physicShape: PhysicShape = sceneObjectJson.shape
    private val armature: String = sceneObjectJson.armature
    private var collisionFilterMask: List<Int> = sceneObjectJson.collisionFilterMask
    private val mass: Float = sceneObjectJson.mass

    val node: String = sceneObjectJson.nodes
    var collisionFilterGroup: Int = sceneObjectJson.collisionFilterGroup
    val instantiate: Boolean = sceneObjectJson.instantiate
    var animation: String = sceneObjectJson.animation
    var hide: Boolean = sceneObjectJson.hide

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