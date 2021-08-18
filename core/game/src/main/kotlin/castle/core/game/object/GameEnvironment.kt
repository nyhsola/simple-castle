package castle.core.game.`object`

import castle.core.game.GameContext
import castle.core.game.`object`.unit.GameObject
import com.badlogic.gdx.utils.Disposable

class GameEnvironment(gameContext: GameContext) : Disposable {
    private val environment: List<GameObject> = gameContext.resourceService.constructorMap
        .filter { entry -> entry.value.instantiate }
        .map { entry -> GameObject(entry.value, gameContext) }

    override fun dispose() {
        environment.forEach { it.dispose() }
    }
}