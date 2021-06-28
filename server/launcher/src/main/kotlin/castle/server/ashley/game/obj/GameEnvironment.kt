package castle.server.ashley.game.obj

import castle.server.ashley.game.GameContext
import castle.server.ashley.game.obj.unit.GameObject
import com.badlogic.gdx.utils.Disposable

class GameEnvironment(gameContext: GameContext) : Disposable {
    private val environment: List<GameObject> = gameContext.resourceManager.constructorMap
        .filter { entry -> entry.value.instantiate }
        .map { entry -> GameObject(entry.value, gameContext) }

    override fun dispose() {
        environment.forEach { it.dispose() }
    }
}