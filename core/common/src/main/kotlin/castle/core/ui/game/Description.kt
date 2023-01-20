package castle.core.ui.game

import castle.core.component.MapComponent
import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.service.CommonResources
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.Align

class Description(
        commonResources: CommonResources
) : TextArea("", commonResources.skin) {
    private var track: Entity? = null

    init {
        alignment = Align.center
    }

    fun update() {
        if (track != null) {
            val modelRenderComponent = ModelRenderComponent.mapper.get(track)
            val unitComponent = UnitComponent.mapper.get(track)
            val mapComponent = MapComponent.mapper.get(track)
            val stateComponent = StateComponent.mapper.get(track)
            val currentArea = mapComponent.currentArea
            messageText = """
                ${modelRenderComponent.nodeName} (${stateComponent.state.currentState.javaClass.simpleName})
                - - - - -
                HP: ${unitComponent.currentHealth}/${unitComponent.totalHealth}
                Position: ${currentArea.x} ${currentArea.y}
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