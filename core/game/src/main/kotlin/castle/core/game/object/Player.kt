package castle.core.game.`object`

import castle.core.common.`object`.CommonEntity
import castle.core.game.json.PlayerJson
import com.badlogic.gdx.utils.Disposable

class Player(
    private val playerJson: PlayerJson,
    private val gameContext: GameContext
) : Disposable {
    private val startupUnits: List<CommonEntity> = playerJson.buildStartup(gameContext).onEach { it.add(gameContext.engine) }
    private val currUnits: MutableList<CommonEntity> = ArrayList()

    fun spawn() {
        currUnits.add(playerJson.buildUnit(gameContext).add(gameContext.engine))
    }

    override fun dispose() {
        currUnits.forEach { it.dispose() }
        startupUnits.forEach { it.dispose() }
    }
}