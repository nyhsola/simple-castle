package castle.core.game.`object`

import com.badlogic.gdx.utils.Disposable

class Players(gameContext: GameContext) : Disposable {
    private val players: List<Player> = gameContext.getResourceService().players.map { Player(it, gameContext) }

    fun spawn() {
        players.forEach { it.spawn() }
    }

    override fun dispose() {
        players.forEach { it.dispose() }
    }
}