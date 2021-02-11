package castle.server.ashley.service

import castle.server.ashley.game.Chat
import castle.server.ashley.game.GameMap
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine

data class GameContext(
    val engine: Engine, val resourceManager: ResourceManager, val chat: Chat, val gameMap: GameMap
)