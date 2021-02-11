package castle.server.ashley.game

import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.Disposable

class GameEnvironment(engine: Engine, resourceManager: ResourceManager) : Disposable {
    private val environment: List<GameObject> =
        resourceManager.constructorMap.filter { entry -> entry.value.instantiate }.map { entry -> GameObject(engine, entry.value) }
            .onEach { engine.addEntity(it.entity) }

    override fun dispose() {
        environment.forEach { it.dispose() }
    }
}