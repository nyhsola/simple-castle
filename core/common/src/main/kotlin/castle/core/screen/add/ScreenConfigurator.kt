package castle.core.screen.add

import castle.core.system.GameManagerSystem
import castle.core.system.PhysicSystem
import castle.core.system.UnitSystem
import castle.core.system.render.*
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

class ScreenConfigurator(entitySystems: List<EntitySystem>) {
    companion object {
        val order = linkedSetOf(
                AnimationRenderSystem::class.java,
                ModelRenderSystem::class.java,
                CircleRenderSystem::class.java,
                LineRenderSystem::class.java,
                TextRenderSystem::class.java,
                HpRenderSystem::class.java,
                StageRenderSystem::class.java,
                PhysicSystem::class.java,
                UnitSystem::class.java,
                GameManagerSystem::class.java
        )
    }

    val engine: PooledEngine = PooledEngine()
    val inputMultiplexer = InputMultiplexer()
    val screens = ArrayList<KtxScreen>()
    val disposables = ArrayList<Disposable>()

    init {
        entitySystems.forEach { withSystem(it) }
    }

    private fun withSystem(entitySystem: EntitySystem) {
        if (entitySystem is Disposable) {
            disposables.add(entitySystem)
        }
        if (entitySystem is KtxScreen) {
            screens.add(entitySystem)
        }
        if (entitySystem is KtxInputAdapter) {
            inputMultiplexer.addProcessor(entitySystem)
        }
        entitySystem.priority = order.indexOf(entitySystem.javaClass)
        engine.addSystem(entitySystem)
    }
}