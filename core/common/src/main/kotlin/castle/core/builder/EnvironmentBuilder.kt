package castle.core.builder

import castle.core.json.EnvironmentJson
import castle.core.service.CommonResources
import com.badlogic.ashley.core.Entity

class EnvironmentBuilder(
        private val commonResources: CommonResources,
        private val templateBuilder: TemplateBuilder
) {
    fun build(environmentJson: EnvironmentJson): List<Entity> {
        val templateJson = commonResources.templates.getValue(environmentJson.templateName)
        val nodes = getNodes(environmentJson.nodesPattern)
        return nodes.map { templateBuilder.build(templateJson, it) }
    }

    private fun getNodes(nodesPattern: String): Collection<String> {
        return commonResources.model
                .flatMap { it.value.nodes }
                .map { node -> node.id }
                .toSet()
                .filter { nodes -> nodes.matches(Regex(nodesPattern)) }
    }
}