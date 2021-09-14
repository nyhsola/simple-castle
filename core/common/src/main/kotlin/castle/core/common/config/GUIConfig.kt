package castle.core.common.config

import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GUIConfig(commonConfig: CommonConfig) : Disposable {
    val modelBatch = ModelBatch()
    val decalBatch = DecalBatch(CameraGroupStrategy(commonConfig.cameraService.currentCamera.camera))
    val stage = Stage(ScreenViewport())
    val shapeRender = ShapeRenderer().apply { setAutoShapeType(true) }

    override fun dispose() {
        modelBatch.dispose()
        decalBatch.dispose()
        shapeRender.dispose()
        stage.dispose()
    }
}