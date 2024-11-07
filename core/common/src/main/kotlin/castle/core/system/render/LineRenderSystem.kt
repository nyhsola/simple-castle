package castle.core.system.render

import castle.core.component.render.LineRenderComponent
import castle.core.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import org.koin.core.annotation.Single

@Single
class LineRenderSystem(
    private val shapeRenderer: ShapeRenderer,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(LineRenderComponent::class.java).get()) {
    private val transformMatrix4 = Matrix4()

    override fun update(deltaTime: Float) {
        shapeRenderer.transformMatrix = transformMatrix4
        shapeRenderer.projectionMatrix = cameraService.currentCamera.camera.combined
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST)
        shapeRenderer.begin()
        super.update(deltaTime)
        shapeRenderer.end()
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val lineRenderComponent = LineRenderComponent.mapper.get(entity)
        if (lineRenderComponent.show) {
            shapeRenderer.set(ShapeRenderer.ShapeType.Line)
            shapeRenderer.color = lineRenderComponent.color
            shapeRenderer.line(lineRenderComponent.from, lineRenderComponent.to)
        }
    }
}