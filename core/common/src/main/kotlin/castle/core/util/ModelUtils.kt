package castle.core.util

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.Vector3

interface ModelUtils {
    companion object {
        val DEFAULT_FACE_DIRECTION: Vector3 = Vector3(1f, 0f, 0f)

        fun searchNodes(models: Map<String, Model>, nodesPattern: String): Collection<String> {
            return models
                .flatMap { it.value.nodes }
                .map { node -> node.id }
                .toSet()
                .filter { nodes -> nodes.matches(Regex(nodesPattern)) }
        }
    }
}