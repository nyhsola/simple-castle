package castle.core.system

import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.service.*
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import org.koin.core.annotation.Single

@Single
class GameManagerSystem(
    private val environmentService: EnvironmentService,
    private val gameService: GameService,
    private val uiService: UIService,
    private val selectionService: SelectionService,
    private val cameraService: CameraService,
    private val mapScanService: MapScanService,
    private val mapService: MapService,
    private val eventQueue: EventQueue
) : IntervalSystem(GAME_TICK), KtxInputAdapter, KtxScreen {
    companion object {
        private const val GAME_TICK: Float = 0.1f
        const val CHAT_FOCUSED = "CHAT_FOCUSED"
        const val CHAT_UNFOCUSED = "CHAT_UNFOCUSED"
        const val EXIT_GAME = "EXIT_GAME"
    }

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(CHAT_FOCUSED) { cameraService.input = false },
        Pair(CHAT_UNFOCUSED) { cameraService.input = true },
        Pair(EXIT_GAME) { Gdx.app.postRunnable(Gdx.app::exit) }
    )

    private val inputMultiplexer = InputMultiplexer()

    override fun addedToEngine(engine: Engine) {
        environmentService.init()
        mapScanService.init()
        mapService.init()
        uiService.init()
        selectionService.init()
        gameService.init()
        inputMultiplexer.addProcessor(cameraService)
        inputMultiplexer.addProcessor(uiService)
        inputMultiplexer.addProcessor(gameService)
        inputMultiplexer.addProcessor(selectionService)
    }

    override fun update(deltaTime: Float) {
        cameraService.update(deltaTime)
        super.update(deltaTime)
    }

    override fun updateInterval() {
        uiService.update(mapService.unitsInArea)
        gameService.update(GAME_TICK)
        mapService.update()
        eventQueue.proceed(operations)
    }

    override fun resize(width: Int, height: Int) {
        cameraService.resize(width, height)
    }

    override fun keyDown(keycode: Int): Boolean {
        inputMultiplexer.keyDown(keycode)
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        inputMultiplexer.keyTyped(character)
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        inputMultiplexer.keyUp(keycode)
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        inputMultiplexer.mouseMoved(screenX, screenY)
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        inputMultiplexer.scrolled(amountX, amountY)
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        inputMultiplexer.touchDown(screenX, screenY, pointer, button)
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        inputMultiplexer.touchDragged(screenX, screenY, pointer)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        inputMultiplexer.touchUp(screenX, screenY, pointer, button)
        return false
    }

    override fun dispose() {
        environmentService.dispose()
        gameService.dispose()
        uiService.dispose()
    }
}