package castle.server.ashley.game

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.game.event.EventContext
import castle.server.ashley.game.event.EventQueue
import castle.server.ashley.game.event.EventType
import castle.server.ashley.game.obj.Chat
import castle.server.ashley.game.obj.GameEnvironment
import castle.server.ashley.game.obj.GameMap
import castle.server.ashley.game.obj.Players
import castle.server.ashley.game.service.ScanService
import castle.server.ashley.system.service.CameraService
import castle.server.ashley.system.service.PhysicService
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.utils.Disposable

class GameManager(
    engine: Engine,
    guiCreator: GUICreator,
    private val physicService: PhysicService,
    private val cameraService: CameraService
) : Disposable {
    private val resourceManager: ResourceManager = ResourceManager()
    private val scanService = ScanService(physicService)
    private val gameEnvironment = GameEnvironment(engine, resourceManager)
    private val gameMap: GameMap = GameMap(engine, Gdx.graphics.width, Gdx.graphics.height, scanService, resourceManager)
    private val gameContext: GameContext = GameContext(engine, resourceManager, gameMap)

    private val eventQueue = EventQueue()
    private val signal = Signal<EventContext>().apply { this.add(eventQueue) }

    private val chat = Chat(engine, guiCreator, signal, resourceManager)
    private val players: Players = Players(gameContext)

    fun update(delta: Float) {
        proceedEvents()
        gameMap.update(players.getUnits())
        players.update(delta)
        proceedMessages()
        dispatchAiMessages(delta)
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