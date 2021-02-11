package castle.server.ashley.utils

import castle.server.ashley.utils.json.PlayerJson
import castle.server.ashley.utils.json.SceneObjectJson
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.utils.Json

class SceneLoader(model: Model) {
    private val json = Json()
    private val nodeNames = model.nodes.map { node -> node.id }.toSet()

    private fun getValuesByPattern(pattern: String): Collection<String> {
        return nodeNames.filter { nodes -> nodes.matches(Regex(pattern)) }
    }

    fun loadSceneObjects(): List<SceneObjectJson> {
        val sceneObjectsJson = json.fromJson(
            List::class.java, SceneObjectJson::class.java, SceneLoader::class.java.getResource("/game-scene-objects.json").readText()
        )

        return sceneObjectsJson.map { any -> any as SceneObjectJson }
            .map { sceneObjectJson -> Pair(sceneObjectJson, getValuesByPattern(sceneObjectJson.nodes)) }
            .map { pair -> pair.second.map { nodeName -> pair.first.copy(nodes = nodeName) } }.flatten()
    }

    fun loadPlayers(): List<PlayerJson> {
        return json.fromJson(List::class.java, PlayerJson::class.java, SceneLoader::class.java.getResource("/players.json").readText())
            .map { any -> any as PlayerJson }
    }
}