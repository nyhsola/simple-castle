package castle.core.game

import castle.core.game.utils.ResourceManager
import com.badlogic.ashley.core.Engine

data class GameContext(
    val engine: Engine,
    val resourceManager: ResourceManager
)