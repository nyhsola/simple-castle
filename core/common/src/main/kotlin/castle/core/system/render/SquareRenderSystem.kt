package castle.core.system.render

import castle.core.component.render.SquareRenderComponent
import castle.core.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class SquareRenderSystem(
    private val shapeRenderer: ShapeRenderer,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(SquareRenderComponent::class.java).get()) {
    private val tempVector3 = Vector3()
    private val tempMatrix4 = Matrix4()

    override fun update(deltaTime: Float) {
        shapeRenderer.projectionMatrix = cameraService.currentCamera.camera.combined
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST)
        shapeRenderer.begin()
        super.update(deltaTime)
        shapeRenderer.end()
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val squareRenderComponent = SquareRenderComponent.mapper.get(entity)
        tempMatrix4.setTranslation(squareRenderComponent.matrix.getTranslation(tempVector3))
        tempMatrix4.translate(squareRenderComponent.vector3Offset)
        shapeRenderer.set(squareRenderComponent.shapeType)
        shapeRenderer.transformMatrix = tempMatrix4
        shapeRenderer.color = squareRenderComponent.color
        shapeRenderer.rotate(1f, 0f, 0f, 90f)
        shapeRenderer.rect(0f, 0f, squareRenderComponent.width * 0.9f, squareRenderComponent.height * 0.9f)
    }
}