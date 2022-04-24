package castle.core.builder

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.json.TemplateJson
import castle.core.`object`.CommonEntity
import castle.core.service.CommonResources
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array
import java.util.*

class TemplateBuilder(private val commonResources: CommonResources) {
    fun build(templateJson: TemplateJson, nodeName: String): CommonEntity {
        val commonEntity = CommonEntity()
        return buildUnitInternal(commonEntity, templateJson, nodeName)
    }

    private fun buildUnitInternal(commonEntity: CommonEntity, templateJson: TemplateJson, nodeName: String): CommonEntity {
        val positionComponent = initPosition(nodeName)
        commonEntity.add(positionComponent)
        commonEntity.add(initRender(templateJson, nodeName, positionComponent))
        commonEntity.add(initPhysic(templateJson, nodeName))
        return commonEntity
    }

    private fun initPosition(node: String): PositionComponent {
        return PositionComponent(getMatrix4(node))
    }

    private fun initRender(templateJson: TemplateJson, nodeName: String, positionComponent: PositionComponent): ModelRenderComponent {
        val modelInstance = getModelInstance(nodeName)
        val hide = templateJson.hide
        modelInstance.transform = positionComponent.matrix4
        return ModelRenderComponent(modelInstance, hide, nodeName)
    }

    private fun initPhysic(templateJson: TemplateJson, nodeName: String): PhysicComponent {
        val physicShape = templateJson.shape
        val shape = physicShape.build(getModel(nodeName))
        val info = btRigidBody.btRigidBodyConstructionInfo(templateJson.mass, null, shape)
        val collisionFlag = templateJson.collisionFlag
        val collisionFilterGroupParam = templateJson.collisionFilterGroup
        val collisionFilterMaskList = templateJson.collisionFilterMask
        val physicComponent = PhysicComponent(info, collisionFlag, collisionFilterGroupParam, collisionFilterMaskList)
        physicComponent.body.userData = nodeName + "-" + UUID.randomUUID().toString()
        return physicComponent
    }

    private fun getMatrix4(node: String): Matrix4 {
        return findNode(node)!!.getNode(node).globalTransform.cpy()
    }

    private fun getModelInstance(node: String): ModelInstance {
        return getModel(node).apply {
            transform.mul(model.getNode(node).localTransform)
            nodes.forEach { node ->
                node.translation.set(0f, 0f, 0f)
                node.scale.set(1f, 1f, 1f)
                calculateTransforms()
            }
        }
    }

    private fun getModel(nodeName: String): ModelInstance {
        val nodeModelStr = "$nodeName-model"
        val list = mutableListOf(nodeName)
        val nodeModel = findNode(nodeModelStr)
        nodeModel?.let { list.add(nodeModelStr) }
        return ModelInstance(nodeModel ?: findNode(nodeName), Array(list.toTypedArray()))
    }

    private fun findNode(node: String) : Model? {
        Gdx.app.logLevel = Application.LOG_NONE;
        Gdx.app.log("log", commonResources.model.map { it.key }.joinToString { it })
        return commonResources.model.filter { it.value.getNode(node) != null }.map { it.value }.firstOrNull()
    }
}