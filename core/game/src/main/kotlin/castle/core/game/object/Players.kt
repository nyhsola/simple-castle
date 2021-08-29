package castle.core.game.`object`

import castle.core.game.GameContext
import castle.core.game.`object`.unit.GameObject
import castle.core.game.service.MapService
import com.badlogic.gdx.utils.Disposable

class Players(
    gameContext: GameContext,
    private val mapService: MapService
) : Disposable {
    private val players: List<Player> = gameContext.resourceService.players.map { Player(it, gameContext, mapService) }

    fun update(delta: Float) {
        players.forEach { it.update(delta) }
    }

    fun spawn() {
        players.forEach { it.spawn() }
    }

    fun getUnits(): List<GameObject> {
        return players.map { it.getUnits() }.flatten()
    }

    override fun dispose() {
        players.forEach { it.dispose() }
    }
}