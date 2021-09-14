package castle.core.common.json

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.common.json.settings.PhysicSettings
import castle.core.common.json.settings.RenderSettings
import castle.core.common.physic.PhysicInstance
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array
import java.util.*

data class TemplateJson(
    val templateName: String = "",
    private val nodeName: String = "",
    private val renderSettings: RenderSettings = RenderSettings(false),
    private val physicSettings: PhysicSettings = PhysicSettings(false)
) {
    fun buildUnit(model: Model): CommonEntity {
        val commonEntity = CommonEntity()
        return buildUnitInternal(commonEntity, model, nodeName)
    }

    fun buildUnit(model: Model, nodeNameParam: String): CommonEntity {
        val commonEntity = CommonEntity()
        return buildUnitInternal(commonEntity, model, nodeNameParam)
    }

    private fun buildUnitInternal(commonEntity: CommonEntity, model: Model, node: String): CommonEntity {
        val positionComponent = initPosition(model, node)
        commonEntity.add(positionComponent)

        if (renderSettings.enabled) {
            val renderComponent = initRender(model, node, positionComponent)
            commonEntity.add(renderComponent)
        }

        if (physicSettings.enabled) {
            val physicComponent = initPhysic(model, node)
            commonEntity.add(physicComponent)
        }

        return commonEntity
    }

    private fun initPosition(model: Model, node: String): PositionComponent {
        return PositionComponent(getMatrix4(model, node))
    }

    private fun initRender(model: Model, node: String, positionComponent: PositionComponent): RenderComponent {
        val modelInstance = getModelInstance(model, node, renderSettings.armature)
        modelInstance.transform = positionComponent.matrix4
        return RenderComponent(modelInstance, renderSettings.hide, node, renderSettings.armature)
    }

    private fun initPhysic(model: Model, node: String): PhysicComponent {
        val physicComponent = PhysicComponent(getPhysicInstance(model, node, renderSettings.armature))
        physicComponent.physicInstance.body.userData = node + "-" + UUID.randomUUID().toString()
        return physicComponent
    }

    private fun getPhysicInstance(model: Model, node: String, armature: String): PhysicInstance {
        val shape = physicSettings.shape.build(getModel(model, node, armature))
        val info = btRigidBody.btRigidBodyConstructionInfo(physicSettings.mass, null, shape)
        return PhysicInstance(info, physicSettings.collisionFlag, physicSettings.collisionFilterGroup, physicSettings.collisionFilterMask)
    }

    private fun getMatrix4(model: Model, node: String): Matrix4 {
        return model.getNode(node).globalTransform.cpy()
    }

    private fun getModelInstance(model: Model, node: String, armature: String): ModelInstance {
        return getModel(model, node, armature).apply {
            transform.mul(model.getNode(node).localTransform)
            nodes.forEach { node ->
                node.translation.set(0f, 0f, 0f)
                node.scale.set(1f, 1f, 1f)
                calculateTransforms()
            }
        }
    }

    private fun getModel(model: Model, node: String, armature: String): ModelInstance {
        val array = if (armature.isNotEmpty()) listOf(armature, node).toTypedArray() else listOf(node).toTypedArray()
        return ModelInstance(model, Array(array))
    }
}