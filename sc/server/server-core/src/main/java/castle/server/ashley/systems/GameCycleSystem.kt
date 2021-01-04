package castle.server.ashley.systems

import castle.server.ashley.service.HighLevelGameEventService
import castle.server.ashley.systems.adapter.IntervalSystemAdapter
import com.badlogic.ashley.core.Engine

class GameCycleSystem(private val highLevelGameEventService: HighLevelGameEventService) : IntervalSystemAdapter(1000f) {

    override fun addedToEngine(engine: Engine) {
        highLevelGameEventService.startGame(engine)
    }

    override fun updateInterval() {

    }
}