package castle.core.game.`object`

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.RenderComponent
import castle.core.game.service.GameResourceService
import com.badlogic.gdx.utils.Disposable

class GameEnvironment(gameResourceService: GameResourceService) : Disposable {
    val environment: Map<String, CommonEntity> = gameResourceService.environment
        .map { entry -> entry.buildStartup(gameResourceService) }
        .flatten()
        .associateBy { RenderComponent.mapper.get(it).nodeName }

    override fun dispose() {
        environment.forEach { it.value.dispose() }
    }
}