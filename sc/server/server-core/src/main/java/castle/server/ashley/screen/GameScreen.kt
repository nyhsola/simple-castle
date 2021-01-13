package castle.server.ashley.screen

import castle.server.ashley.creator.GUICreator
import castle.server.ashley.event.EventContext
import castle.server.ashley.event.EventQueue
import castle.server.ashley.service.MiniMapService
import castle.server.ashley.service.PhysicService
import castle.server.ashley.service.PlayerService
import castle.server.ashley.systems.*
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector3

class GameScreen(guiCreator: GUICreator) : InputScreenAdapter() {
    private val resourceManager: ResourceManager = ResourceManager()
    private val camera: Camera = PerspectiveCamera().apply {
        near = 1f
        far = 300f
        fieldOfView = 67f
        viewportWidth = Gdx.graphics.width.toFloat()
        viewportHeight = Gdx.graphics.height.toFloat()
        position.set(Vector3(10f, 10f, 0f))
        lookAt(Vector3.Zero)
    }
    private val environment: Environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    init {
        val eventQueue = EventQueue()
        val signal = Signal<EventContext>().apply { add(eventQueue) }

        val physicService = PhysicService(camera)
        val playerService = PlayerService(resourceManager)
        val miniMapService = MiniMapService(playerService, physicService)

        customEngine.apply {
            addSystem(CameraControlSystem(camera))
            addSystem(RenderSystem(camera, environment, guiCreator))
            addSystem(PhysicSystem(physicService))
            addSystem(AnimationSystem())
            addSystem(DebugSystem(resourceManager, camera))
            addSystem(GameCycleSystem(miniMapService, playerService, eventQueue, signal))
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit()
        }
        return super.keyDown(keycode)
    }

    override fun dispose() {
        resourceManager.dispose()
        super.dispose()
    }

}
