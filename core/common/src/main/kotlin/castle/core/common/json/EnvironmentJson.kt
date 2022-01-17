package castle.core.common.json

import castle.core.common.`object`.CommonEntity
import castle.core.common.service.ResourceService
import com.badlogic.gdx.graphics.g3d.Model

data class EnvironmentJson(
    private val templateName: String = "",
    private val nodesPattern: String = ""
) {
    fun buildStartup(gameResourceService: ResourceService) : List<CommonEntity> {
        val templates = gameResourceService.templates
        val template = templates.getValue(templateName)
        val model = gameResourceService.model
        val nodes = getNodes(model)
        return nodes.map { template.buildUnit(model, it) }
    }

    private fun getNodes(model: Model): Collection<String> {
        return model.nodes
            .map { node -> node.id }
            .toSet()
            .filter { nodes -> nodes.matches(Regex(nodesPattern)) }
    }
}