package castle.core.common.builder

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.common.json.TemplateJson
import castle.core.common.physic.PhysicInstance
import castle.core.common.service.CommonResources
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array
import java.util.*

class TemplateBuilder(private val commonResources: CommonResources) {
    fun build(templateJson: TemplateJson): CommonEntity {
        val commonEntity = CommonEntity()
        return buildUnitInternal(commonEntity, templateJson, templateJson.nodeName)
    }

    fun build(templateJson: TemplateJson, nodeName: String): CommonEntity {
        val commonEntity = CommonEntity()
        return buildUnitInternal(commonEntity, templateJson, nodeName)
    }

    private fun buildUnitInternal(commonEntity: CommonEntity, templateJson: TemplateJson, nodeName: String): CommonEntity {
        val positionComponent = initPosition(nodeName)
        commonEntity.add(positionComponent)

        if (templateJson.renderSettings.enabled) {
            val renderComponent = initRender(templateJson, nodeName, positionComponent)
            commonEntity.add(renderComponent)
        }

        if (templateJson.physicSettings.enabled) {
            val physicComponent = initPhysic(templateJson, nodeName)
            commonEntity.add(physicComponent)
        }

        return commonEntity
    }

    private fun initPosition(node: String): PositionComponent {
        return PositionComponent(getMatrix4(node))
    }

    private fun initRender(templateJson: TemplateJson, nodeName: String, positionComponent: PositionComponent): RenderComponent {
        val armature = templateJson.renderSettings.armature
        val modelInstance = getModelInstance(nodeName, armature)
        val hide = templateJson.renderSettings.hide
        val hideOnMap = templateJson.renderSettings.hideOnMap
        modelInstance.transform = positionComponent.matrix4
        return RenderComponent(modelInstance, hide, hideOnMap, nodeName, armature)
    }

    private fun initPhysic(templateJson: TemplateJson, nodeName: String): PhysicComponent {
        val physicComponent = PhysicComponent(getPhysicInstance(templateJson, nodeName))
        physicComponent.physicInstance.body.userData = nodeName + "-" + UUID.randomUUID().toString()
        return physicComponent
    }

    private fun getPhysicInstance(templateJson: TemplateJson, nodeName: String): PhysicInstance {
        val armature = templateJson.renderSettings.armature
        val physicShape = templateJson.physicSettings.shape
        val shape = physicShape.build(getModel(nodeName, armature))
        val info = btRigidBody.btRigidBodyConstructionInfo(templateJson.physicSettings.mass, null, shape)
        val collisionFlag = templateJson.physicSettings.collisionFlag
        val collisionFilterGroupParam = templateJson.physicSettings.collisionFilterGroup
        val collisionFilterMaskList = templateJson.physicSettings.collisionFilterMask
        return PhysicInstance(info, collisionFlag, collisionFilterGroupParam, collisionFilterMaskList)
    }

    private fun getMatrix4(node: String): Matrix4 {
        val model = commonResources.model.filter { it.value.getNode(node) != null }.map { it.value }.first()
        return model.getNode(node).globalTransform.cpy()
    }

    private fun getModelInstance(node: String, armature: String): ModelInstance {
        return getModel(node, armature).apply {
            transform.mul(model.getNode(node).localTransform)
            nodes.forEach { node ->
                node.translation.set(0f, 0f, 0f)
                node.scale.set(1f, 1f, 1f)
                calculateTransforms()
            }
        }
    }

    private fun getModel(node: String, armature: String): ModelInstance {
        val model = commonResources.model.filter { it.value.getNode(node) != null }.map { it.value }.first()
        val array = if (armature.isNotEmpty()) listOf(armature, node).toTypedArray() else listOf(node).toTypedArray()
        return ModelInstance(model, Array(array))
    }
}