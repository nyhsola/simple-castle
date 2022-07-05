package castle.core.service

import castle.core.json.DecorationJson
import castle.core.json.EnvironmentJson
import castle.core.json.TemplateJson
import castle.core.util.LoadUtils
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.JsonReader
import org.koin.core.annotation.Single

@Single
class CommonResources : Disposable {
    private val assets3d = listOf("map.g3dj", "castle.g3dj", "tower.g3dj", "projectile.g3dj", "unit-warrior.g3dj")
    private val assets2d = listOf("castle.png", "hp.png", "unit-warrior.png", "start.png")

    private val loader = G3dModelLoader(JsonReader())
    val model: Map<String, Model> = loadModel()
    val skin: Skin = Skin(Gdx.files.internal("ui/uiskin.json"))
    val templates: Map<String, TemplateJson> = LoadUtils.loadList("/template.json", TemplateJson::class.java).associateBy { it.templateName }
    val textures: Map<String, Texture> = loadTextures()
    val environment: List<EnvironmentJson> = LoadUtils.loadList("/environment.json", EnvironmentJson::class.java)
    val decoration: List<DecorationJson> = LoadUtils.loadList("/decoration.json", DecorationJson::class.java)

    private fun loadModel(): Map<String, Model> {
        val map = HashMap<String, Model>()
        for (entry in assets3d) {
            map[entry] = loader.loadModel(Gdx.files.internal("assets3d/$entry"))
        }
        return map
    }

    private fun loadTextures(): Map<String, Texture> {
        val map = HashMap<String, Texture>()
        for (entry in assets2d) {
            map[entry] = Texture(Gdx.files.internal("assets2d/$entry"))
        }
        return map
    }

    override fun dispose() {
        model.forEach { it.value.dispose() }
        textures.forEach { it.value.dispose() }
        skin.dispose()
    }
}