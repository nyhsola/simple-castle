package castle.core.common.service

import castle.core.common.json.EnvironmentJson
import castle.core.common.json.TemplateJson
import castle.core.common.util.LoadUtils
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.JsonReader

class CommonResources : Disposable {
    private val loader = G3dModelLoader(JsonReader())
    val model: Model = loadModel()
    val skin: Skin = loadSkin()
    val templates: Map<String, TemplateJson> = loadTemplates().associateBy { it.templateName }
    val textures: Map<String, Texture> = loadTextures()
    val environment: List<EnvironmentJson> = loadEnvironment()

    private fun loadModel(): Model {
        return loader.loadModel(Gdx.files.internal("assets3d/map.g3dj"))
    }

    private fun loadSkin(): Skin {
        return Skin(Gdx.files.internal("ui/uiskin.json"))
    }

    private fun loadTemplates(): List<TemplateJson> {
        return LoadUtils.loadList("/template.json", TemplateJson::class.java)
    }

    private fun loadEnvironment(): List<EnvironmentJson> {
        return LoadUtils.loadList("/environment.json", EnvironmentJson::class.java)
    }

    private fun loadTextures(): Map<String, Texture> {
        val map = HashMap<String, Texture>()
        val fileHandle = Gdx.files.getFileHandle("assets2d", Files.FileType.Internal)
        for (entry in fileHandle.list()) {
            map[entry.nameWithoutExtension()] = Texture(entry)
        }
        return map
    }

    override fun dispose() {
        model.dispose()
        skin.dispose()
        textures.forEach { it.value.dispose() }
    }
}