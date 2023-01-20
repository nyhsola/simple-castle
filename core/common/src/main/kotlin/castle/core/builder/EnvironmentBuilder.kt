package castle.core.builder

import castle.core.json.EnvironmentJson
import castle.core.service.CommonResources
import castle.core.util.ModelUtils
import com.badlogic.ashley.core.Entity
import org.koin.core.annotation.Single

@Single
class EnvironmentBuilder(
        private val commonResources: CommonResources,
        private val templateBuilder: TemplateBuilder
) {
    fun build(): List<Entity> {
        return commonResources.environment
                .map { buildInternal(it) }
                .flatten()
    }

    private fun buildInternal(environmentJson: EnvironmentJson): List<Entity> {
        val nodes = ModelUtils.searchNodes(commonResources.model, environmentJson.nodesPattern)
        return nodes.map { templateBuilder.build(environmentJson.templateName, it) }
    }
}