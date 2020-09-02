package castle.server.ashley.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.JsonReader

class AssetLoader {
    companion object {
        private val LOADER = G3dModelLoader(JsonReader())
    }

    fun loadModel(): Model {
        return LOADER.loadModel(Gdx.files.internal("models/map.g3dj"))
    }

    fun loadSkin(): Skin {
        return Skin(Gdx.files.internal("ui/uiskin.json"))
    }
}