package castle.core.util

import com.badlogic.gdx.graphics.g3d.Model

interface ModelUtils {
    companion object {
        fun searchNodes(models: Map<String, Model>, nodesPattern: String): Collection<String> {
            return models
                    .flatMap { it.value.nodes }
                    .map { node -> node.id }
                    .toSet()
                    .filter { nodes -> nodes.matches(Regex(nodesPattern)) }
        }
    }
}