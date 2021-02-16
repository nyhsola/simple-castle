package castle.server.ashley.systems.render

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import castle.server.ashley.systems.component.RectComponent
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class RectRenderSystem(guiCreator: GUICreator) : IteratingSystemAdapter(Family.all(RectComponent::class.java).get()) {
    private val shapeRenderer = guiCreator.createShapeRenderer().apply {
        setAutoShapeType(true)
    }

    override fun render(delta: Float) {
        shapeRenderer.begin()
        for (i in 0 until entities.size()) {
            val rectComponent = RectComponent.mapper.get(entities[i])
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = rectComponent.color
            shapeRenderer.rect(rectComponent.position.x, rectComponent.position.y, rectComponent.width, rectComponent.height)
        }
        shapeRenderer.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}