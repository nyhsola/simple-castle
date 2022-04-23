package castle.core.service

import castle.core.builder.EffectBuilder
import castle.core.builder.PlayerBuilder
import castle.core.event.EventQueue
import castle.core.service.game.Player
import com.badlogic.ashley.core.Engine

class GameService(
    gameResources: GameResources,
    private val playerBuilder: PlayerBuilder,
    private val effectBuilder: EffectBuilder
) {
    companion object {
        const val SPAWN: String = "SPAWN"
        const val PLAYER_NAME: String = "PLAYER_NAME"
    }

    private val players = gameResources.players.mapValues { Player(it.value, playerBuilder, effectBuilder) }

    fun init(engine: Engine) {
        players.onEach { it.value.init(engine) }
    }

    fun update(engine: Engine, deltaTime: Float, eventQueue: EventQueue) {
        players.onEach { it.value.update(engine, deltaTime) }
        proceedEvents(eventQueue, engine)
    }

    private fun proceedEvents(eventQueue: EventQueue, engine: Engine) {
        eventQueue.proceed { eventContext ->
            when (eventContext.eventType) {
                SPAWN -> {
                    val playerName = eventContext.params[PLAYER_NAME] as String
                    players.getValue(playerName).spawnUnits(engine)
                    true
                }
                else -> false
            }
        }
    }
}