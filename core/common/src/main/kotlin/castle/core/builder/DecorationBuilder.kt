package castle.core.builder

import castle.core.component.MapComponent
import castle.core.json.DecorationJson
import castle.core.service.CommonResources
import castle.core.util.ModelUtils
import com.badlogic.ashley.core.Entity
import org.koin.core.annotation.Single

@Single
class DecorationBuilder(
    private val commonResources: CommonResources,
    private val templateBuilder: TemplateBuilder
) {
    fun build(): List<Entity> {
        return commonResources.decoration
            .map { buildInternal(it) }
            .flatten()
    }

    private fun buildInternal(decorationJson: DecorationJson): List<Entity> {
        val nodes = ModelUtils.searchNodes(commonResources.model, decorationJson.nodesPattern)
        return nodes.map { buildInternal(decorationJson.templateName, it) }
    }

    private fun buildInternal(templateName: String, node: String): Entity {
        val entity = templateBuilder.build(templateName, node)
        entity.add(MapComponent(false))
        return entity
    }
}