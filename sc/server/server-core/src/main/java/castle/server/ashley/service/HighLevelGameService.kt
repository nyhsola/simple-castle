package castle.server.ashley.service

import com.badlogic.ashley.core.Engine

class HighLevelGameService(private val mapService: MapService, private val playerService: PlayerService) {
    fun startGame(engine: Engine) {
        playerService.createGameEnvironment(engine)
        playerService.initializePlayers(engine)

        mapService.createMinimap(engine)
    }

    fun tick(engine: Engine, deltaTime: Float) {
        playerService.spawnUnitsForPlayers(engine, deltaTime)
    }

}