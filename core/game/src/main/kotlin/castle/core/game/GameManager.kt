package castle.core.game

import castle.core.common.creator.GUIConfig
import castle.core.common.service.CameraService
import castle.core.game.`object`.Chat
import castle.core.game.`object`.GameEnvironment
import castle.core.game.`object`.GameMap
import castle.core.game.`object`.Players
import castle.core.game.event.EventContext
import castle.core.game.event.EventQueue
import castle.core.game.event.EventType
import castle.core.game.service.RayCastService
import castle.core.game.service.ScanService
import castle.core.game.utils.ResourceManager
import castle.core.physic.service.PhysicService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

class GameManager(
    engine: Engine,
    guiConfig: GUIConfig,
    private val physicService: PhysicService,
    private val cameraService: CameraService
) : KtxInputAdapter, Disposable {
    private val resourceManager: ResourceManager = ResourceManager()
    private val scanService = ScanService(physicService)
    private val rayCastService = RayCastService(physicService, cameraService)
    private val gameContext: GameContext = GameContext(engine, resourceManager)

    private val gameEnvironment = GameEnvironment(gameContext)
    private val gameMap: GameMap =
        GameMap(engine, Gdx.graphics.width, Gdx.graphics.height, scanService, resourceManager)

    private val eventQueue = EventQueue()
    private val signal = Signal<EventContext>().apply { this.add(eventQueue) }

    private val chat = Chat(engine, guiConfig, signal, resourceManager)
    private val players: Players = Players(gameContext, gameMap)

    private val temp = Vector3()

    fun update(delta: Float) {
        proceedEvents()
        gameMap.update(players.getUnits())
        players.update(delta)
        proceedMessages()
        dispatchAiMessages(delta)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val collisionObject = rayCastService.rayCast(screenX.toFloat(), screenY.toFloat())
        if (collisionObject != null) {
            collisionObject.worldTransform.getTranslation(temp)

            val nodeStr = collisionObject.userData as String
            val position = "X: ${temp.x}\nY:${temp.y}\nZ: ${temp.z}"

            chat.typeMessage("$nodeStr\n$position")
        }
        return super.touchDown(screenX, screenY, pointer, button)
    }

    private fun proceedEvents() {
        val gameEvents = eventQueue.pollAll()
        for (gameEvent in gameEvents) {
            when (gameEvent.eventType) {
                EventType.CHAT_FOCUSED -> {
                    cameraService.input = false
                }
                EventType.CHAT_UNFOCUSED -> {
                    cameraService.input = true
                }
            }
        }
    }

    private fun proceedMessages() {
        val messages = chat.pollAllMessages()
        for (message in messages) {
            if (message == "debug-physic") {
                physicService.debugEnabled = !physicService.debugEnabled
            }
            if (message == "debug-ui") {
                chat.debugEnabled = !chat.debugEnabled
            }
        }
    }

    private fun dispatchAiMessages(delta: Float) {
        GdxAI.getTimepiece().update(delta)
        MessageManager.getInstance().update()
    }

    override fun dispose() {
        resourceManager.dispose()
        gameEnvironment.dispose()
        chat.dispose()
        players.dispose()
    }
}