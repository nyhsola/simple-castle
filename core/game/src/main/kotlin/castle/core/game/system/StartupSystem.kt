package castle.core.game.system

import castle.core.game.service.NeutralInitService
import castle.core.game.service.PlayerInitService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem

class StartupSystem(
    private val neutralInitService: NeutralInitService,
    private val playerInitService: PlayerInitService,
) : EntitySystem() {
    override fun addedToEngine(engine: Engine) {
        neutralInitService.init(engine)
        playerInitService.init(engine)
    }
}