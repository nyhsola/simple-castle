package castle.core.common.builder

import castle.core.common.`object`.CommonEntity
import castle.core.common.json.EnvironmentJson
import castle.core.common.service.CommonResources

class EnvironmentBuilder(
    private val commonResources: CommonResources,
    private val templateBuilder: TemplateBuilder
) {
    fun build(environmentJson: EnvironmentJson): List<CommonEntity> {
        val templateJson = commonResources.templates.getValue(environmentJson.templateName)
        val nodes = getNodes(environmentJson.nodesPattern)
        return nodes.map { templateBuilder.build(templateJson, it) }
    }

    private fun getNodes(nodesPattern: String): Collection<String> {
        return commonResources.model.nodes
            .map { node -> node.id }
            .toSet()
            .filter { nodes -> nodes.matches(Regex(nodesPattern)) }
    }
}