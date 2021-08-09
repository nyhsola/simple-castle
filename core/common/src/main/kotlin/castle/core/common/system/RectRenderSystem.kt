package castle.core.common.system

import castle.core.common.creator.GUIConfig
import castle.core.common.component.RectComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable

class RectRenderSystem(guiConfig: GUIConfig) : IteratingSystem(Family.all(RectComponent::class.java).get()), Disposable {
    private val shapeRenderer = guiConfig.shapeRenderer().apply {
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