package castle.core.system.render

import castle.core.component.render.CircleRenderComponent
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
class CircleRenderSystem(
        private val shapeRenderer: ShapeRenderer,
        private val cameraService: CameraService
) : IteratingSystem(Family.all(CircleRenderComponent::class.java).get()) {
    private val tempVector3 = Vector3()
    private val tempMatrix4 = Matrix4()

    override fun update(deltaTime: Float) {
        shapeRenderer.projectionMatrix = cameraService.currentCamera.camera.combined
        shapeRenderer.begin()
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        super.update(deltaTime)
        shapeRenderer.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val circleRenderComponent = CircleRenderComponent.mapper.get(entity)
        tempMatrix4.setTranslation(circleRenderComponent.matrix4Track.getTranslation(tempVector3))
        tempMatrix4.translate(circleRenderComponent.vector3Offset)
        shapeRenderer.set(circleRenderComponent.shapeType)
        shapeRenderer.transformMatrix = tempMatrix4
        shapeRenderer.color = circleRenderComponent.color
        shapeRenderer.rotate(1f, 0f, 0f, 90f)
        shapeRenderer.circle(0f, 0f, circleRenderComponent.radius, 50)
    }
}