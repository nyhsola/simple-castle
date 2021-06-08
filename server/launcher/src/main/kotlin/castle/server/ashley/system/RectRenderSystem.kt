package castle.server.ashley.system

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.system.component.RectComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable

class RectRenderSystem(guiCreator: GUICreator) : IteratingSystem(Family.all(RectComponent::class.java).get()), Disposable {
    private val shapeRenderer = guiCreator.createShapeRenderer().apply {
        setAutoShapeType(true)
    }

    override fun update(deltaTime: Float) {
        shapeRenderer.begin()
        for (i in 0 until entities.size()) {
            val rectComponent = RectComponent.mapper.get(entities[i])
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = rectComponent.color
            shapeRenderer.rect(rectComponent.position.x, rectComponent.position.y, rectComponent.width, rectComponent.height)
        }
        shapeRenderer.end()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}