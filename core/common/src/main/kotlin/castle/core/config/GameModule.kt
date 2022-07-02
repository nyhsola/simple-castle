package castle.core.config

import castle.core.service.CameraService
import castle.core.service.CommonResources
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import org.koin.core.annotation.ComponentScan
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import org.koin.ksp.generated.module

@org.koin.core.annotation.Module
@ComponentScan("castle.core")
class GameModule : Disposable {
    private val cameraService: CameraService by inject(CameraService::class.java)
    private val spriteBatch: SpriteBatch by inject(SpriteBatch::class.java)
    private val commonResources: CommonResources by inject(CommonResources::class.java)
    private val modelBatch: ModelBatch by inject(ModelBatch::class.java)
    private val shapeRenderer: ShapeRenderer by inject(ShapeRenderer::class.java)
    private val decalBatch: DecalBatch by inject(DecalBatch::class.java)

    private val defaultModule = module {
        single { PooledEngine() } bind Engine::class
        single { ModelBatch() }
        single { SpriteBatch() }
        single { DecalBatch(CameraGroupStrategy(cameraService.currentCamera.camera)) }
        single { ShapeRenderer().apply { setAutoShapeType(true) } }
        single { ScalingViewport(Scaling.stretch, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), OrthographicCamera()) } bind Viewport::class
    }

    fun start() {
        startKoin {
            modules(defaultModule)
            modules(module)
        }
    }

    override fun dispose() {
        spriteBatch.dispose()
        commonResources.dispose()
        modelBatch.dispose()
        shapeRenderer.dispose()
        decalBatch.dispose()
    }
}