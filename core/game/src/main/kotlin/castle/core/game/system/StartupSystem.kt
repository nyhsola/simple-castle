package castle.core.game.system

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.RenderComponent
import castle.core.game.component.PlayerComponent
import castle.core.game.service.GameResourceService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem

class StartupSystem(private val gameResourceService: GameResourceService) : EntitySystem() {
    override fun addedToEngine(engine: Engine) {
        gameResourceService.environment
            .map { entry -> entry.buildStartup(gameResourceService) }
            .flatten()
            .associateBy { RenderComponent.mapper.get(it).nodeName }
            .forEach { it.value.add(engine) }
        gameResourceService.players
            .map { PlayerComponent(it) }
            .map { CommonEntity().apply { add(it) } }
            .forEach { it.add(engine) }
    }
}