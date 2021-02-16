package castle.server.ashley.systems

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.game.event.EventContext
import castle.server.ashley.game.event.EventQueue
import castle.server.ashley.game.event.EventType
import castle.server.ashley.service.CameraService
import castle.server.ashley.service.GameManager
import castle.server.ashley.service.MapService
import castle.server.ashley.service.PhysicService
import castle.server.ashley.systems.adapter.IntervalInputSystemAdapter
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager

class GameCycleSystem(
    private val resourceManager: ResourceManager,
    private val guiCreator: GUICreator,
    private val mapService: MapService,
    private val physicService: PhysicService,
    private val cameraService: CameraService,
    private val signal: Signal<EventContext>
) : IntervalInputSystemAdapter(GAME_TICK) {
    private companion object {
        const val GAME_TICK: Float = 0.5f
    }

    private val eventQueue = EventQueue()

    private lateinit var gameManager: GameManager

    override fun addedToEngine(engine: Engine) {
        gameManager = GameManager(engine, resourceManager, guiCreator, mapService, signal)
        signal.add(eventQueue)
    }

    override fun updateInterval() {
        proceedEvents()
        gameManager.update(GAME_TICK)
        proceedMessages()
        dispatchAiMessages()
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
        val messages = gameManager.chat.pollAllMessages()
        for (message in messages) {
            if (message == "debug-physic") {
                physicService.debugEnabled = !physicService.debugEnabled
            }
            if (message == "debug-ui") {
                gameManager.chat.debugEnabled = !gameManager.chat.debugEnabled
            }
        }
    }

    private fun dispatchAiMessages() {
        GdxAI.getTimepiece().update(GAME_TICK)
        MessageManager.getInstance().update()
    }

    override fun dispose() {
        gameManager.dispose()
    }
}