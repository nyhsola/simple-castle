package castle.server.ashley.service

import com.badlogic.ashley.core.Engine

class HighLevelGameEventService(private val playerService: PlayerService) {
    fun startGame(engine: Engine) {
        playerService.createGameEnvironment(engine)
    }
}