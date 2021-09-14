package castle.core.game.`object`

import castle.core.common.`object`.BaseContext
import castle.core.game.service.GameResourceService
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.Disposable

class GameContext(
    engine: Engine,
    val gameEnvironment: GameEnvironment,
    private val gameResourceService: GameResourceService
) : BaseContext(engine, gameResourceService), Disposable {

    init {
        gameEnvironment.environment.forEach { it.value.add(engine) }
    }

    override fun getResourceService() = gameResourceService

    override fun dispose() {
        gameEnvironment.environment.forEach { it.value.remove(engine) }
    }
}