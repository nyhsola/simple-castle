package castle.server.ashley.game.obj

import castle.server.ashley.game.obj.unit.GameObject
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.Disposable

class GameEnvironment(engine: Engine, resourceManager: ResourceManager) : Disposable {
    private val environment: List<GameObject> = resourceManager.constructorMap
        .filter { entry -> entry.value.instantiate }
        .map { entry -> GameObject(entry.value, engine) }

    override fun dispose() {
        environment.forEach { it.dispose() }
    }
}