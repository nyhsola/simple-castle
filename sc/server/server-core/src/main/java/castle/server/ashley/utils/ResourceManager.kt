package castle.server.ashley.utils

import castle.server.ashley.utils.json.PlayerJson
import castle.server.ashley.utils.json.SceneObjectJson
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonReader

class ResourceManager : Disposable {
    private val loader = G3dModelLoader(JsonReader())
    private val json = Json()

    val model: Model = loadModel()
    val skin: Skin = loadSkin()
    val sceneObjectsJson: List<SceneObjectJson> = loadSceneObjects()
    val players: List<PlayerJson> = loadPlayers()

    private fun loadModel(): Model {
        return loader.loadModel(Gdx.files.internal("models/map.g3dj"))
    }

    private fun loadSkin(): Skin {
        return Skin(Gdx.files.internal("ui/uiskin.json"))
    }

    private fun loadSceneObjects(): List<SceneObjectJson> {
        val sceneObjectsJson = json.fromJson(List::class.java,
                SceneObjectJson::class.java,
                SceneLoader::class.java.getResource("/game-scene-objects.json").readText())
        return sceneObjectsJson.map { any -> any as SceneObjectJson }
                .map { sceneObjectJson -> Pair(sceneObjectJson, getValuesByPattern(sceneObjectJson.nodes)) }
                .map { pair -> pair.second.map { nodeName -> pair.first.copy(nodes = nodeName) } }.flatten()
    }

    private fun loadPlayers(): List<PlayerJson> {
        return json.fromJson(List::class.java, PlayerJson::class.java, SceneLoader::class.java.getResource("/players.json").readText())
                .map { any -> any as PlayerJson }
    }

    private fun getValuesByPattern(pattern: String): Collection<String> {
        return model.nodes.map { node -> node.id }.toSet().filter { nodes -> nodes.matches(Regex(pattern)) }
    }

    override fun dispose() {
        model.dispose()
        skin.dispose()

    }
}