package castle.server.ashley.game.obj

import castle.server.ashley.game.GameContext
import castle.server.ashley.game.obj.unit.GameObject
import com.badlogic.gdx.utils.Disposable

class Players(gameContext: GameContext) : Disposable {
    private val players: List<Player> = gameContext.resourceManager.players.map { Player(it, gameContext) }

    fun update(delta: Float) {
        players.forEach { it.update(delta) }
    }

    fun getUnits(): List<GameObject> {
        return players.map { it.getUnits() }.flatten()
    }

    override fun dispose() {
        players.forEach { it.dispose() }
    }
}