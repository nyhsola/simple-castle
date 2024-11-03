package castle.core.ui.game

import castle.core.component.MapComponent
import castle.core.component.PositionComponent
import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.service.CommonResources
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.Align

class Description(
    commonResources: CommonResources
) : TextArea("", commonResources.skin) {
    private var track: Entity? = null
    private val tempVector3 = Vector3()
    private val twoNumberFormat = "%.2f"

    init {
        alignment = Align.center
    }

    fun update() {
        if (track != null) {
            val modelRenderComponent = ModelRenderComponent.mapper.get(track)
            val unitComponent = UnitComponent.mapper.get(track)
            val positionComponent = PositionComponent.mapper.get(track)
            val mapComponent = MapComponent.mapper.get(track)
            val stateComponent = StateComponent.mapper.get(track)
            val currentArea = mapComponent.currentArea
            val vector3 = positionComponent.matrix4.getTranslation(tempVector3)
            messageText = """
                ${modelRenderComponent.nodeName} (${stateComponent.state.currentState.javaClass.simpleName})
                - - - - - - - - - - - - - -
                HP: ${unitComponent.currentHealth}/${unitComponent.totalHealth}
                Position: ${currentArea.x} ${currentArea.y}
                Position (World): x:${twoNumberFormat.format(vector3.x)} y:${twoNumberFormat.format(vector3.y)} z:${twoNumberFormat.format(vector3.z)}
                Angle: ${positionComponent.faceDirection}
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