package castle.core.service

import castle.core.builder.PlayerBuilder
import castle.core.event.EventQueue
import com.badlogic.ashley.core.Engine

class PlayerService(
        gameResources: GameResources,
        private val playerBuilder: PlayerBuilder
) {
    companion object {
        const val SPAWN: String = "SPAWN"
        const val PLAYER_NAME: String = "PLAYER_NAME"
    }

    private val players = gameResources.players

    fun init(engine: Engine) {
        players.map { playerBuilder.buildBuildings(it.value) }
                .flatten()
                .onEach { it.add(engine) }

    }

    fun update(engine: Engine, eventQueue: EventQueue) {
        eventQueue.proceed { eventContext ->
            when (eventContext.eventType) {
                SPAWN -> {
                    val playerName = eventContext.params[PLAYER_NAME] as String
                    val playerJson = players.getValue(playerName)
                    playerBuilder.buildUnits(playerJson).onEach { it.add(engine) }
                    true
                }
                else -> false
            }
        }
    }
}