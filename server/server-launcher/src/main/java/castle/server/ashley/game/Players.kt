package castle.server.ashley.game

import castle.server.ashley.service.GameContext
import com.badlogic.gdx.utils.Disposable

class Players(gameContext: GameContext) : Disposable {
    private val players: List<Player> = gameContext.resourceManager.players.map { Player(it, gameContext) }

    fun update(delta: Float) {
        players.forEach { it.update(delta) }
    }

    override fun dispose() {
        players.forEach { it.dispose() }
    }

}