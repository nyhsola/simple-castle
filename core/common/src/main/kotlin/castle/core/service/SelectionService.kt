package castle.core.service

import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.CircleRenderComponent
import castle.core.component.render.ModelRenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.utils.Disposable
import org.koin.core.annotation.Single

@Single
class SelectionService(
    private val engine: Engine,
    private val uiService: UIService,
    private val rayCastService: RayCastService
) : Disposable {
    private val selectionCircle: Entity = Entity().add(CircleRenderComponent())
    private val radiusCircle: Entity = Entity().add(CircleRenderComponent())
    private val boundingBox = BoundingBox()
    private val tempVector3 = Vector3()
    private val color = Color(Color.GREEN).apply { a = 0.3f }

    fun init() {
        engine.addEntity(selectionCircle)
        engine.addEntity(radiusCircle)
    }

    fun select(x: Int, y: Int) {
        val userPick = rayCastService.rayCast(x.toFloat(), y.toFloat())?.userData
        if (userPick == null) unSelect() else select(userPick as Entity)
    }

    private fun select(selectedUnit: Entity) {
        formSelectionCircle(selectedUnit)
        formRadiusCircle(selectedUnit)
        uiService.activateSelection(selectedUnit)
    }

    private fun formSelectionCircle(selectedUnit: Entity) {
        val matrix4 = PositionComponent.mapper.get(selectedUnit).matrix4
        val modelInstance = ModelRenderComponent.mapper.get(selectedUnit).modelInstance
        modelInstance.calculateBoundingBox(boundingBox).getDimensions(tempVector3)
        val radius = listOf(tempVector3.x, tempVector3.z).minOf { it } / 2f
        val height = -tempVector3.y / 2
        val circleRenderComponent = CircleRenderComponent.mapper.get(selectionCircle)
        circleRenderComponent.radius = radius + radius * 0.4f
        circleRenderComponent.matrix4Track = matrix4
        circleRenderComponent.vector3Offset.set(0f, height - height * 0.1f, 0f)
    }

    private fun formRadiusCircle(selectedUnit: Entity) {
        val matrix4 = PositionComponent.mapper.get(selectedUnit).matrix4
        val modelInstance = ModelRenderComponent.mapper.get(selectedUnit).modelInstance
        val radius = UnitComponent.mapper.get(selectedUnit).visibilityRange
        modelInstance.calculateBoundingBox(boundingBox).getDimensions(tempVector3)
        val height = -tempVector3.y / 2
        val circleRenderComponent = CircleRenderComponent.mapper.get(radiusCircle)
        circleRenderComponent.radius = radius
        circleRenderComponent.color = color
        circleRenderComponent.shapeType = ShapeRenderer.ShapeType.Filled
        circleRenderComponent.matrix4Track = matrix4
        circleRenderComponent.vector3Offset.set(0f, height - height * 0.1f, 0f)
    }

    private fun unSelect() {
        CircleRenderComponent.mapper.get(selectionCircle).radius = 0.0f
        CircleRenderComponent.mapper.get(radiusCircle).radius = 0.0f
        uiService.deactivateSelection()
    }

    override fun dispose() {
        engine.removeEntity(selectionCircle)
    }
}