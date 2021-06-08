package castle.server.ashley.app.screen

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.system.*
import castle.server.ashley.system.service.CameraService
import castle.server.ashley.system.service.PhysicService
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxScreen

class GameScreen(guiCreator: GUICreator) : KtxScreen {
    private val engine: PooledEngine = PooledEngine()
    private val inputMultiplexer = InputMultiplexer()
    private val disposables = ArrayList<Disposable>()

    private val cameraService = CameraService()
    private val physicService = PhysicService(cameraService)

    init {
        val cameraControlSystem = CameraControlSystem(cameraService)
        val modelRenderSystem = ModelRenderSystem(guiCreator, cameraService)
        val rectRenderSystem = RectRenderSystem(guiCreator)
        val line3DRenderSystem = Line3DRenderSystem(guiCreator, cameraService)
        val stageRenderSystem = StageRenderSystem()
        val physicSystem = PhysicSystem(physicService)
        val animationSystem = AnimationSystem()
        val gameCycleSystem = GameCycleSystem(guiCreator, physicService, cameraService)

        engine.apply {
            addSystem(cameraControlSystem)
            addSystem(modelRenderSystem)
            addSystem(line3DRenderSystem)
            addSystem(rectRenderSystem)
            addSystem(stageRenderSystem)
            addSystem(physicSystem)
            addSystem(animationSystem)
            addSystem(gameCycleSystem)
        }

        inputMultiplexer.addProcessor(cameraControlSystem)
        inputMultiplexer.addProcessor(stageRenderSystem)
        inputMultiplexer.addProcessor(gameCycleSystem)

        disposables.add(modelRenderSystem)
        disposables.add(rectRenderSystem)
        disposables.add(line3DRenderSystem)
        disposables.add(physicSystem)
        disposables.add(gameCycleSystem)
    }

    override fun resize(width: Int, height: Int) {
        cameraService.resize(width, height)
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun show() {
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun dispose() {
        engine.removeAllEntities()
        disposables.forEach { it.dispose() }
    }
}
