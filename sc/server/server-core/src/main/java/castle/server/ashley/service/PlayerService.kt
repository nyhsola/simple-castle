package castle.server.ashley.service

import castle.server.ashley.game.BaseUnit
import castle.server.ashley.game.Player
import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine

class PlayerService(private val resourceManager: ResourceManager) {
    val constructorMap: Map<String, Constructor> =
            resourceManager.sceneObjectsJson.map { sceneObjectJson -> Constructor(resourceManager.model, sceneObjectJson) }
                    .associateBy(keySelector = { constructor -> constructor.node })
                    .toMap()

    fun createGameEnvironment(engine: Engine) {
        constructorMap.filter { entry -> entry.value.instantiate }.forEach { entry -> BaseUnit(engine, entry.value) }
    }

    fun createPlayers(): List<Player> {
        return resourceManager.players.map { Player(it, constructorMap) }
    }

}