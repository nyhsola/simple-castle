package castle.core.service

import castle.core.builder.DecorationBuilder
import castle.core.builder.PlayerBuilder
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.`object`.Player
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Disposable
import org.koin.core.annotation.Single

@Single
class GameService(
    private val engine: Engine,
    private val eventQueue: EventQueue,
    private val playerBuilder: PlayerBuilder,
    private val decorationBuilder: DecorationBuilder
) : Disposable {
    companion object {
        const val DEBUG_SPAWN: String = "DEBUG_SPAWN"
        const val PLAYER_NAME: String = "PLAYER_NAME"
    }

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(DEBUG_SPAWN) {
            val playerName = it.params[PLAYER_NAME] as String
            players.getValue(playerName).spawnUnits()
        }
    )

    private val players: MutableMap<String, Player> = HashMap()
    private val decorations: MutableList<Entity> = ArrayList()

    fun init() {
        players.putAll(playerBuilder.build())
        decorations.addAll(decorationBuilder.build())

        players.onEach { it.value.init() }
        decorations.onEach { engine.addEntity(it) }
    }

    fun update(deltaTime: Float) {
        players.onEach { it.value.update(deltaTime) }
        eventQueue.proceed(operations)
    }

    override fun dispose() {
        players.forEach { it.value.dispose() }
        decorations.forEach { engine.removeEntity(it) }
    }
}