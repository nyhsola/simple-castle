package castle.core.config

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

class ScreenConfig(
    val engine: Engine,
    entitySystems: LinkedHashSet<EntitySystem>
) {
    val inputMultiplexer = InputMultiplexer().apply { entitySystems.filter { it is KtxInputAdapter }.forEach { addProcessor(it as InputProcessor) } }
    val screens = ArrayList<KtxScreen>(entitySystems.filter { it is KtxScreen }.map { it as KtxScreen })
    val disposables = ArrayList(entitySystems.filter { it is Disposable }.map { it as Disposable }.reversed())

    init {
        entitySystems.forEachIndexed { index, entitySystem ->
            entitySystem.priority = index
            engine.addSystem(entitySystem)
        }
    }
}