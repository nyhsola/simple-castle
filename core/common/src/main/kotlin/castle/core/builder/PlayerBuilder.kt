package castle.core.builder

import castle.core.event.EventQueue
import castle.core.`object`.Player
import castle.core.service.GameResources
import com.badlogic.ashley.core.Engine
import org.koin.core.annotation.Single

@Single
class PlayerBuilder(
    private val gameResources: GameResources,
    private val unitBuilder: UnitBuilder,
    private val textBuilder: TextBuilder,
    private val eventQueue: EventQueue,
    private val engine: Engine
) {
    fun build(): Map<String, Player> {
        return gameResources.players.mapValues { Player(eventQueue, engine, it.value, unitBuilder, textBuilder) }
    }
}