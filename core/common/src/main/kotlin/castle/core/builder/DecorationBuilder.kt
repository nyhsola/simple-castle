package castle.core.builder

import castle.core.behaviour.Behaviours
import castle.core.component.StateComponent
import castle.core.json.DecorationJson
import castle.core.json.TemplateJson
import castle.core.service.CommonResources
import castle.core.util.ModelUtils
import com.badlogic.ashley.core.Entity

class DecorationBuilder(
    private val commonResources: CommonResources,
    private val templateBuilder: TemplateBuilder,
    private val behaviours: Behaviours
) {
    private companion object {
        private const val decorationBehaviour: String = "decoration"
    }

    fun buildDecorations(): List<Entity> {
        return commonResources.decoration
            .map { buildInternal(it) }
            .flatten()
    }

    private fun buildInternal(decorationJson: DecorationJson): List<Entity> {
        val templateJson = commonResources.templates.getValue(decorationJson.templateName)
        return ModelUtils.searchNodes(commonResources.model, decorationJson.nodesPattern)
            .map { buildInternal(templateJson, it) }
    }

    private fun buildInternal(templateJson: TemplateJson, node: String): Entity {
        val entity = templateBuilder.build(templateJson, node)
        entity.add(
            StateComponent(
                behaviours.behaviors.getValue(decorationBehaviour),
                entity
            )
        )
        return entity
    }
}