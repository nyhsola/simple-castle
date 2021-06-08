package castle.server.ashley.game

import castle.server.ashley.game.obj.GameMap
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine

data class GameContext(
    val engine: Engine,
    val resourceManager: ResourceManager,
    val gameMap: GameMap
)