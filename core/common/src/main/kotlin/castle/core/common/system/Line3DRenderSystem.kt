package castle.core.common.system

import castle.core.common.component.Line3DComponent
import castle.core.common.config.GUIConfig
import castle.core.common.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable

class Line3DRenderSystem(
    guiConfig: GUIConfig,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(Line3DComponent::class.java).get()), Disposable {
    private val shapeRenderer = guiConfig.createShapeRender()

    override fun update(deltaTime: Float) {
        shapeRenderer.projectionMatrix = cameraService.currentCamera.camera.combined
        shapeRenderer.begin()
        super.update(deltaTime)
        shapeRenderer.end()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val line3DComponent = Line3DComponent.mapper.get(entity)
        if (line3DComponent.show) {
            shapeRenderer.set(ShapeRenderer.ShapeType.Line)
            shapeRenderer.color = line3DComponent.color
            shapeRenderer.line(line3DComponent.from, line3DComponent.to)
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}