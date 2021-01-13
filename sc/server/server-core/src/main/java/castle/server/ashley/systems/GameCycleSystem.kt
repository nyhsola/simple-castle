package castle.server.ashley.systems

import castle.server.ashley.event.EventContext
import castle.server.ashley.event.EventQueue
import castle.server.ashley.event.EventType
import castle.server.ashley.game.Player
import castle.server.ashley.service.MiniMapService
import castle.server.ashley.service.PlayerService
import castle.server.ashley.systems.adapter.IntervalSystemAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager

class GameCycleSystem(private val miniMapService: MiniMapService, private val playerService: PlayerService, private val eventQueue: EventQueue,
                      private val signal: Signal<EventContext>) : IntervalSystemAdapter(GAME_TICK) {
    companion object {
        const val GAME_TICK: Float = 1f
    }

    private val players: MutableList<Player> = ArrayList()

    override fun addedToEngine(engine: Engine) {
        signal.dispatch(EventContext(EventType.GAME_START))
    }

    override fun updateInterval() {
        val gameEvents = eventQueue.pollAll()
        for (gameEvent in gameEvents) {
            when (gameEvent.eventType) {
                EventType.GAME_START -> gameStart()
                EventType.POST_GAME_START -> miniMapService.createMinimap(engine)
            }
        }
        players.forEach { it.update(engine, GAME_TICK) }
        GdxAI.getTimepiece().update(GAME_TICK)
        MessageManager.getInstance().update()
    }

    private fun gameStart() {
        playerService.createGameEnvironment(engine)
        players.addAll(playerService.createPlayers())
        signal.dispatch(EventContext(EventType.POST_GAME_START))
    }
}