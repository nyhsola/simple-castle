package castle.core.game

import castle.core.common.service.ResourceService
import com.badlogic.ashley.core.Engine

data class GameContext(
    val engine: Engine,
    val resourceService: ResourceService
)