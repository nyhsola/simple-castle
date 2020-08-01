package castle.server.ashley.systems

import castle.server.ashley.component.AnimationComponent
import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.screen.ConstructorManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

class InitSystem(private val constructorManager: ConstructorManager) : SystemAdapter() {
    override fun addedToEngine(engine: Engine) {
        init()
        super.addedToEngine(engine)
    }

    private fun init() {
        constructorManager.constructorMap
                .asIterable()
                .filter { entry -> entry.value.instantiate }
                .forEach { entry ->
                    run {
                        val entity: Entity = engine.createEntity()
                        val positionComponent = PositionComponent.createComponent(engine, entry.value)
                        entity.add(positionComponent)

                        if (!entry.value.hide) {
                            val renderComponent = RenderComponent.createComponent(engine, entry.value)
                            entity.add(renderComponent)
                        }

                        if (entry.value.animation.isNotEmpty()) {
                            val animationComponent = AnimationComponent.createComponent(engine, entry.value)
                            entity.add(animationComponent)
                        }

                        val physicComponent = PhysicComponent.createComponent(engine, entry.value)
                        entity.add(physicComponent)

                        engine.addEntity(entity)
                    }
                }
    }
}