package castle.core.service

import castle.core.component.PositionComponent
import castle.core.component.render.CircleRenderComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.ui.service.UIService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class SelectionService(
    private val uiService: UIService,
    private val rayCastService: RayCastService
) {
    private val circle: Entity = Entity().add(CircleRenderComponent())
    private val boundingBox = BoundingBox()
    private val tempVector3 = Vector3()

    fun init(engine: Engine) {
        engine.addEntity(circle)
    }

    fun select(x: Int, y: Int) {
        val userPick = rayCastService.rayCast(x.toFloat(), y.toFloat())?.userData
        if (userPick == null) {
            unSelect()
        } else {
            select(userPick as Entity)
        }
    }

    private fun select(selectedUnit: Entity) {
        val circleRenderComponent = CircleRenderComponent.mapper.get(circle)
        val matrix4 = PositionComponent.mapper.get(selectedUnit).matrix4
        val modelInstance = ModelRenderComponent.mapper.get(selectedUnit).modelInstance
        modelInstance.calculateBoundingBox(boundingBox).getDimensions(tempVector3)
        circleRenderComponent.radius = listOf(tempVector3.x, tempVector3.z, tempVector3.y).minOf { it }
        circleRenderComponent.matrix4Track = matrix4
        circleRenderComponent.vector3Offset.set(0f, -tempVector3.y / 2, 0f)
        uiService.activateSelection(selectedUnit)
    }

    private fun unSelect() {
        CircleRenderComponent.mapper.get(circle).radius = 0.0f
        uiService.deactivateSelection()
    }
}