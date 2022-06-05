package castle.core.builder

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.json.TemplateJson
import castle.core.service.CommonResources
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Array

class TemplateBuilder(private val commonResources: CommonResources) {
    fun build(templateName: String, nodeName: String): Entity {
        return buildInternal(Entity(), commonResources.templates.getValue(templateName), nodeName)
    }

    private fun buildInternal(entity: Entity, templateJson: TemplateJson, nodeName: String): Entity {
        val positionComponent = PositionComponent(nodeName, getMatrix4(nodeName))
        entity.add(positionComponent)
        entity.add(initRender(templateJson, nodeName, positionComponent))
        entity.add(initPhysic(entity, templateJson, nodeName))
        return entity
    }

    private fun initRender(templateJson: TemplateJson, nodeName: String, positionComponent: PositionComponent): ModelRenderComponent {
        val modelInstance = getModelInstance(nodeName)
        val hide = templateJson.hide
        modelInstance.transform = positionComponent.matrix4
        return ModelRenderComponent(modelInstance, hide, nodeName)
    }

    private fun initPhysic(entity: Entity, templateJson: TemplateJson, nodeName: String): PhysicComponent {
        val physicComponent = PhysicComponent(getModel(nodeName), templateJson)
        physicComponent.body.userData = entity
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

    private fun findNode(node: String): Model? {
        return commonResources.model.filter { it.value.getNode(node) != null }.map { it.value }.firstOrNull()
    }
}