package castle.server.ashley.systems

import castle.server.ashley.component.PlayerComponent
import castle.server.ashley.service.ConstructorManagerService
import castle.server.ashley.service.PlayerService
import castle.server.ashley.systems.adapter.SystemAdapter
import com.badlogic.ashley.core.Engine

class InitSystem(private val constructorManagerService: ConstructorManagerService,
                 private val playerService: PlayerService) : SystemAdapter() {
    override fun addedToEngine(engine: Engine) {
        init()
        super.addedToEngine(engine)
    }

    private fun init() {
        constructorManagerService.constructorMap
                .asIterable()
                .filter { entry -> entry.value.instantiate }
                .forEach { entry -> engine.addEntity(entry.value.instantiate(engine)) }

        playerService.playersJson.forEach {
            val entity = engine.createEntity()
            val playerComponent = PlayerComponent.createComponent(engine, it)
            entity.add(playerComponent)
            engine.addEntity(entity)
        }
    }
}