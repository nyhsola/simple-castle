package castle.server.ashley.systems.render

import castle.server.ashley.component.Line3DComponent
import castle.server.ashley.creator.GUICreator
import castle.server.ashley.service.CameraService
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class Line3DRenderSystem(guiCreator: GUICreator, private val cameraService: CameraService) :
    IteratingSystemAdapter(Family.all(Line3DComponent::class.java).get()) {

    private val shapeRenderer = guiCreator.createShapeRenderer().apply {
        setAutoShapeType(true)
    }

    override fun render(delta: Float) {
        shapeRenderer.projectionMatrix = cameraService.getCurrentCamera().camera.combined
        shapeRenderer.begin()
        for (i in 0 until entities.size()) {
            val line3DComponent = Line3DComponent.mapper.get(entities[i])
            if (line3DComponent.show) {
                shapeRenderer.set(ShapeRenderer.ShapeType.Line)
                shapeRenderer.color = line3DComponent.color
                shapeRenderer.line(line3DComponent.from, line3DComponent.to)
            }
        }
        shapeRenderer.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}