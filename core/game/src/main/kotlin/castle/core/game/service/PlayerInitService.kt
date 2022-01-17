package castle.core.game.service

import castle.core.game.builder.PlayerBuilder
import com.badlogic.ashley.core.Engine

class PlayerInitService(
    private val gameResources: GameResources,
    private val playerBuilder: PlayerBuilder
) {
    fun init(engine: Engine) {
        gameResources.players
            .map { playerBuilder.buildPlayer(it) }
            .onEach { it.add(engine) }
    }
}