package castle.core.ui.game

import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.service.CommonResources
import castle.core.service.ScanService
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.TextArea

class Description(
        commonResources: CommonResources,
        private val scanService: ScanService
) : TextArea("", commonResources.skin) {
    private var track: Entity? = null
    private val tempVector = Vector3()

    fun update() {
        if (track != null) {
            val modelRenderComponent = ModelRenderComponent.mapper.get(track)
            val positionComponent = PositionComponent.mapper.get(track)
            val unitComponent = UnitComponent.mapper.get(track)
            val area = scanService.toArea(positionComponent.matrix4.getTranslation(tempVector))
            messageText = """
                Unit: ${modelRenderComponent.nodeName}
                Position: ${area.x} ${area.y} 
                Status: ${unitComponent.state.currentState}
            """.trimIndent()
        }
    }

    fun updateDescription(entity: Entity) {
        track = entity
        isVisible = true
    }

    fun resetDescription() {
        messageText = ""
        track = null
        isVisible = false
    }
}