package castle.server.ashley.systems

import castle.server.ashley.event.EventContext
import castle.server.ashley.event.EventQueue
import castle.server.ashley.event.EventType
import castle.server.ashley.game.Chat
import castle.server.ashley.game.Minimap
import castle.server.ashley.game.Player
import castle.server.ashley.service.CameraService
import castle.server.ashley.service.GameCreator
import castle.server.ashley.service.PhysicService
import castle.server.ashley.systems.adapter.IntervalInputSystemAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager

class GameCycleSystem(private val gameCreator: GameCreator, private val physicService: PhysicService, private val cameraService: CameraService,
                      private val signal: Signal<EventContext>) : IntervalInputSystemAdapter(GAME_TICK) {
    companion object {
        const val GAME_TICK: Float = 0.5f
    }

    private val eventQueue = EventQueue()
    private val players: MutableList<Player> = ArrayList()
    private lateinit var chat: Chat
    private lateinit var minimap: Minimap

    override fun addedToEngine(engine: Engine) {
        signal.add(eventQueue)
        signal.dispatch(EventContext(EventType.GAME_START_STEP_1))
    }

    override fun updateInterval() {
        proceedEvents()
        proceedMessages()
        players.forEach { it.update(engine, GAME_TICK) }
        dispatchAiMessages()
    }

    private fun proceedEvents() {
        val gameEvents = eventQueue.pollAll()
        for (gameEvent in gameEvents) {
            when (gameEvent.eventType) {
                EventType.GAME_START_STEP_1 -> {
                    gameCreator.createGameEnvironment(engine)
                    players.addAll(gameCreator.createPlayers())
                    signal.dispatch(EventContext(EventType.GAME_START_STEP_2))
                }
                EventType.GAME_START_STEP_2 -> {
                    minimap = gameCreator.createMinimap(engine)
                    chat = gameCreator.createChat(engine)
                }
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
        if (::chat.isInitialized) {
            val messages = chat.pollAllMessages()
            for (message in messages) {
                if (message == "physic") {
                    physicService.isDebug = !physicService.isDebug
                }
            }
        }
    }

    private fun dispatchAiMessages() {
        GdxAI.getTimepiece().update(GAME_TICK)
        MessageManager.getInstance().update()
    }

    override fun dispose() {
        players.forEach { it.dispose() }
        chat.dispose()
    }
}