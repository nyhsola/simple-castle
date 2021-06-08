package castle.server.ashley.system

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.system.component.Line3DComponent
import castle.server.ashley.system.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable

class Line3DRenderSystem(
    guiCreator: GUICreator,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(Line3DComponent::class.java).get()), Disposable {
    private val shapeRenderer = guiCreator.createShapeRenderer().apply { setAutoShapeType(true) }

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