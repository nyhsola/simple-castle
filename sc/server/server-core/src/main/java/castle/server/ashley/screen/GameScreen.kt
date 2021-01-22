package castle.server.ashley.screen

import castle.server.ashley.creator.GUICreator
import castle.server.ashley.event.EventContext
import castle.server.ashley.service.CameraService
import castle.server.ashley.service.GameCreator
import castle.server.ashley.service.PhysicService
import castle.server.ashley.systems.*
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class GameScreen(guiCreator: GUICreator) : InputScreenAdapter() {
    private val resourceManager: ResourceManager = ResourceManager()

    init {
        val signal = Signal<EventContext>()

        val cameraService = CameraService()
        val physicService = PhysicService(cameraService)
        val gameCreator = GameCreator(resourceManager, guiCreator, physicService, signal)

        customEngine.apply {
            addSystem(CameraControlSystem(cameraService))
            addSystem(ModelRenderSystem(guiCreator, cameraService))
            addSystem(ShapeRenderSystem(guiCreator))
            addSystem(StageRenderSystem())
            addSystem(PhysicSystem(physicService))
            addSystem(AnimationSystem())
            addSystem(GameCycleSystem(gameCreator, physicService, cameraService, signal))
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.ESCAPE -> {
                Gdx.app.exit()
                true
            }
            else -> super.keyDown(keycode)
        }
    }

    override fun dispose() {
        resourceManager.dispose()
        super.dispose()
    }

}
