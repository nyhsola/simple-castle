package castle.core.common.system

import castle.core.common.component.Rect2DComponent
import castle.core.common.config.GUIConfig
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable

class Rect2DRenderSystem(guiConfig: GUIConfig) : IteratingSystem(Family.all(Rect2DComponent::class.java).get()), Disposable {
    private val shapeRenderer = guiConfig.createShapeRender()

    override fun update(deltaTime: Float) {
        shapeRenderer.begin()
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled)
        super.update(deltaTime)
        shapeRenderer.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val rect2DComponent = Rect2DComponent.mapper.get(entity)
        shapeRenderer.color = rect2DComponent.color
        shapeRenderer.rect(rect2DComponent.position.x, rect2DComponent.position.y, rect2DComponent.width, rect2DComponent.height)
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}