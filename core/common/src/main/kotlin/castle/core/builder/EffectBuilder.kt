package castle.core.builder

import castle.core.component.PositionComponent
import castle.core.event.EventContext
import castle.core.json.PlayerJson
import castle.core.service.EnvironmentService
import castle.core.service.game.CountText
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.math.Vector3

class EffectBuilder(
    private val environmentService: EnvironmentService
) {
    fun buildCountText(playerJson: PlayerJson, signal: Signal<EventContext>): List<CountText> {
        return playerJson.paths
            .mapIndexed { lineNumber, it ->
                val pathEntity = environmentService.neutralUnits.getValue(it[0])
                val positionComponent = PositionComponent.mapper.get(pathEntity)
                val position = positionComponent.matrix4.getTranslation(Vector3())
                CountText(position, lineNumber, signal)
            }
    }
}