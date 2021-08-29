package castle.core.common.config

import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GUIConfig : Disposable {
    val modelBatch = ModelBatch()
    val stage = Stage(ScreenViewport())
    val shapeRender = ShapeRenderer().apply { setAutoShapeType(true) }

    override fun dispose() {
        modelBatch.dispose()
        shapeRender.dispose()
        stage.dispose()
    }
}