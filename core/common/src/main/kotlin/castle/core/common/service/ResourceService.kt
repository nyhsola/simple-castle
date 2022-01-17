package castle.core.common.service

import castle.core.common.json.EnvironmentJson
import castle.core.common.json.TemplateJson
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonReader

open class ResourceService : Disposable {
    private val loader = G3dModelLoader(JsonReader())
    protected val json = Json()
    val model: Model = loadModel()
    val skin: Skin = loadSkin()
    val templates: Map<String, TemplateJson> = loadTemplates().associateBy { it.templateName }
    val environment: List<EnvironmentJson> = loadEnvironment()

    private fun loadModel(): Model {
        return loader.loadModel(Gdx.files.internal("assets3d/map.g3dj"))
    }

    private fun loadSkin(): Skin {
        return Skin(Gdx.files.internal("ui/uiskin.json"))
    }

    private fun loadTemplates(): List<TemplateJson> {
        return loadList("/template.json", TemplateJson::class.java)
    }

    private fun loadEnvironment(): List<EnvironmentJson> {
        return loadList("/environment.json", EnvironmentJson::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> loadList(path: String, clazz: Class<T>): List<T> {
        return json.fromJson(List::class.java, clazz, readResource(path)).map { it!! as T }
    }

    private fun readResource(path: String): String {
        return ResourceService::class.java.getResource(path)!!.readText()
    }

    override fun dispose() {
        model.dispose()
        skin.dispose()
    }
}