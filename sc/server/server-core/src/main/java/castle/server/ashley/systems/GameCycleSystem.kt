package castle.server.ashley.systems

import castle.server.ashley.service.PlayerService
import castle.server.ashley.systems.adapter.IntervalSystemAdapter
import com.badlogic.ashley.core.Engine

class GameCycleSystem(
    private val playerService: PlayerService
) : IntervalSystemAdapter(1000f) {
    override fun addedToEngine(engine: Engine) {
        playerService.instantiateStartPositions(engine)
    }

    override fun updateInterval() {

    }
}