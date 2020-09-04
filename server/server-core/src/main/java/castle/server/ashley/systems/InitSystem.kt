package castle.server.ashley.systems

import castle.server.ashley.screen.ConstructorManager
import castle.server.ashley.systems.adapter.SystemAdapter
import com.badlogic.ashley.core.Engine

class InitSystem(private val constructorManager: ConstructorManager) : SystemAdapter() {
    override fun addedToEngine(engine: Engine) {
        init()
        super.addedToEngine(engine)
    }

    private fun init() {
        constructorManager.constructorMap
                .asIterable()
                .filter { entry -> entry.value.instantiate }
                .forEach { entry -> engine.addEntity(entry.value.instantiate(engine)) }
    }
}