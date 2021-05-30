package castle.server.ashley.app.screen

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.game.event.EventContext
import castle.server.ashley.service.CameraService
import castle.server.ashley.service.MapService
import castle.server.ashley.service.PhysicService
import castle.server.ashley.systems.AnimationSystem
import castle.server.ashley.systems.CameraControlSystem
import castle.server.ashley.systems.GameCycleSystem
import castle.server.ashley.systems.PhysicSystem
import castle.server.ashley.systems.render.Line3DRenderSystem
import castle.server.ashley.systems.render.ModelRenderSystem
import castle.server.ashley.systems.render.RectRenderSystem
import castle.server.ashley.systems.render.StageRenderSystem
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxScreen

class GameScreen(guiCreator: GUICreator) : KtxScreen {
    private val engine: PooledEngine = PooledEngine()
    private val inputMultiplexer = InputMultiplexer()
    private val disposables = ArrayList<Disposable>()
    private val resourceManager: ResourceManager = ResourceManager()

    private val cameraService = CameraService()
    private val physicService = PhysicService(cameraService)
    private val mapService = MapService(physicService)

    init {
        val signal = Signal<EventContext>()

        val cameraControlSystem = CameraControlSystem(cameraService)
        val modelRenderSystem = ModelRenderSystem(guiCreator, cameraService)
        val rectRenderSystem = RectRenderSystem(guiCreator)
        val line3DRenderSystem = Line3DRenderSystem(guiCreator, cameraService)
        val stageRenderSystem = StageRenderSystem()
        val physicSystem = PhysicSystem(physicService)
        val animationSystem = AnimationSystem()
        val gameCycleSystem = GameCycleSystem(resourceManager, guiCreator, mapService, physicService, cameraService, signal)

        engine.apply {
            addSystem(cameraControlSystem)
            addSystem(modelRenderSystem)
            addSystem(rectRenderSystem)
            addSystem(line3DRenderSystem)
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
        resourceManager.dispose()
    }
}
