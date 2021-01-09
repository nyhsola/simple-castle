package castle.server.ashley.systems

import castle.server.ashley.service.HighLevelGameService
import castle.server.ashley.systems.adapter.IntervalSystemAdapter
import com.badlogic.ashley.core.Engine

class GameCycleSystem(private val highLevelGameService: HighLevelGameService, private val gameTickPeriod: Float) : IntervalSystemAdapter(gameTickPeriod) {
    override fun addedToEngine(engine: Engine) {
        highLevelGameService.startGame(engine)
    }

    override fun updateInterval() {
        highLevelGameService.tick(engine, gameTickPeriod)
    }
}