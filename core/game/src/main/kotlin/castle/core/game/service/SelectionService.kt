package castle.core.game.service

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.Circle3DComponent
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import java.lang.Float.max

class SelectionService(private val uiService: UIService) {
    val userPick: Entity = CommonEntity().add(Circle3DComponent())

    private val boundingBox = BoundingBox()
    private val tempVector3 = Vector3()

    fun select(selectedUnit: Entity) {
        val circle3DComponent = Circle3DComponent.mapper.get(userPick)
        val matrix4 = PositionComponent.mapper.get(selectedUnit).matrix4
        val modelInstance = RenderComponent.mapper.get(selectedUnit).modelInstance
        modelInstance.calculateBoundingBox(boundingBox).getDimensions(tempVector3)
        circle3DComponent.radius = max(tempVector3.x, tempVector3.z)
        circle3DComponent.matrix4Track = matrix4
        circle3DComponent.vector3Offset.set(0f, 0f, tempVector3.y / 2)
        uiService.changePortrait("unit-warrior-n")
    }

    fun unSelect() {
        Circle3DComponent.mapper.get(userPick).radius = 0.0f
        uiService.changePortrait(null)
    }
}