package castle.core.game.`object`

import castle.core.game.GameContext
import castle.core.game.`object`.unit.GameObject
import castle.core.physic.service.PhysicService
import com.badlogic.gdx.utils.Disposable

class Players(
    gameContext: GameContext,
    private val gameMap: GameMap
) : Disposable {
    private val players: List<Player> = gameContext.resourceManager.players.map { Player(it, gameContext, gameMap) }

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