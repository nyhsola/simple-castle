package castle.core.common.system

import castle.core.common.component.Circle3DComponent
import castle.core.common.config.GUIConfig
import castle.core.common.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class Circle3DRenderSystem(
    guiConfig: GUIConfig,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(Circle3DComponent::class.java).get()) {
    private val shapeRenderer = guiConfig.shapeRender
    private val tempVector3 = Vector3()
    private val tempMatrix4 = Matrix4()
        .setTranslation(Vector3(0f, 0f, 0f))
        .rotate(Vector3.Y, 90f)
        .rotate(Vector3.X, 90f)

    override fun update(deltaTime: Float) {
        shapeRenderer.projectionMatrix = cameraService.currentCamera.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        super.update(deltaTime)
        shapeRenderer.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val circle3DComponent = Circle3DComponent.mapper.get(entity)
        tempMatrix4.setTranslation(circle3DComponent.matrix4Track.getTranslation(tempVector3))
        tempMatrix4.translate(circle3DComponent.vector3Offset)
        shapeRenderer.transformMatrix = tempMatrix4
        shapeRenderer.color = Color.GREEN
        shapeRenderer.circle(0f, 0f, circle3DComponent.radius)
    }
}