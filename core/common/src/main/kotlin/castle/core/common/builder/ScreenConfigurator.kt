package castle.core.common.builder

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

class ScreenConfigurator(entitySystems: List<EntitySystem>) {
    val engine: PooledEngine = PooledEngine()
    val inputMultiplexer = InputMultiplexer()
    val disposables = ArrayList<Disposable>()

    init {
        entitySystems.forEach { withSystem(it) }
    }

    private fun withSystem(entitySystem: EntitySystem) {
        if (entitySystem is Disposable) {
            disposables.add(entitySystem)
        }
        if (entitySystem is KtxInputAdapter) {
            inputMultiplexer.addProcessor(entitySystem)
        }
        engine.addSystem(entitySystem)
    }
}