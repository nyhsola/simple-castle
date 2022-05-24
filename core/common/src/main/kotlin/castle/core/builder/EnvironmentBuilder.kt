package castle.core.builder

import castle.core.json.EnvironmentJson
import castle.core.service.CommonResources
import castle.core.util.ModelUtils
import com.badlogic.ashley.core.Entity

class EnvironmentBuilder(
    private val commonResources: CommonResources,
    private val templateBuilder: TemplateBuilder
) {
    fun buildEnvironment(): List<Entity> {
        return commonResources.environment
            .map { buildInternal(it) }
            .flatten()
    }

    private fun buildInternal(environmentJson: EnvironmentJson): List<Entity> {
        val templateJson = commonResources.templates.getValue(environmentJson.templateName)
        return ModelUtils.searchNodes(commonResources.model, environmentJson.nodesPattern)
            .map { templateBuilder.build(templateJson, it) }
    }
}