package castle.server.ashley.systems

import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.ShapeComponent
import castle.server.ashley.creator.GUICreator
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3

class ShapeRenderSystem(guiCreator: GUICreator) : IteratingSystemAdapter(Family.all(ShapeComponent::class.java, PositionComponent::class.java).get()) {
    private val shapeRenderer = guiCreator.createShapeRenderer().apply {
        setAutoShapeType(true)
    }
    private val tempVector: Vector3 = Vector3()

    override fun render(delta: Float) {
        shapeRenderer.begin()
        for (i in 0 until entities.size()) {
            val positionComponent = PositionComponent.mapper.get(entities[i])
            val shapeComponent = ShapeComponent.mapper.get(entities[i])
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled)
            positionComponent.matrix4.getTranslation(tempVector)
            shapeRenderer.color = shapeComponent.color
            shapeRenderer.rect(tempVector.x, tempVector.y, shapeComponent.width, shapeComponent.height)
        }
        shapeRenderer.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}