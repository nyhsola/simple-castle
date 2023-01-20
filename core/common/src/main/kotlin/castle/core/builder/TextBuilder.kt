package castle.core.builder

import castle.core.component.PositionComponent
import castle.core.event.EventQueue
import castle.core.json.PlayerJson
import castle.core.`object`.CountText
import castle.core.service.EnvironmentService
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class TextBuilder(
        private val environmentService: EnvironmentService
) {
    fun build(playerJson: PlayerJson, eventQueue: EventQueue): List<CountText> {
        return playerJson.paths
                .mapIndexed { lineNumber, it ->
                    val pathEntity = environmentService.environmentObjects.getValue(it[0])
                    val positionComponent = PositionComponent.mapper.get(pathEntity)
                    val position = positionComponent.matrix4.getTranslation(Vector3())
                    CountText(position, lineNumber, eventQueue)
                }
    }
}