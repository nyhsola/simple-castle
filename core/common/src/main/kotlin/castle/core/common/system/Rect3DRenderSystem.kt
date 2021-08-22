package castle.core.common.system

import castle.core.common.component.PositionComponent
import castle.core.common.component.Rect3DComponent
import castle.core.common.config.GUIConfig
import castle.core.common.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class Rect3DRenderSystem(guiConfig: GUIConfig, private val cameraService: CameraService) :
    IteratingSystem(Family.all(PositionComponent::class.java, Rect3DComponent::class.java).get()), EntityListener, Disposable {
    private val shapeRenderer = guiConfig.createShapeRender()
    private val textTransform = Matrix4().idt().rotate(0f, 1f, 0f, 90f)
    private val tempMatrix = Matrix4()
    private val tempVector = Vector3()

    override fun entityAdded(entity: Entity) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val rect3DComponent = Rect3DComponent.mapper.get(entity)
        Rect3DComponent.postConstruct(positionComponent, rect3DComponent)
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun update(deltaTime: Float) {
        shapeRenderer.begin()
        super.update(deltaTime)
        shapeRenderer.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val rect3DComponent = Rect3DComponent.mapper.get(entity)
        positionComponent.matrix4.getTranslation(tempVector)
        val matrix4 = tempMatrix
            .set(cameraService.currentCamera.camera.combined)
            .translate(tempVector.add(rect3DComponent.offset))
            .mul(textTransform)
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = rect3DComponent.color
        shapeRenderer.rect(0f, 0f, rect3DComponent.width, rect3DComponent.height)
        shapeRenderer.projectionMatrix = matrix4
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}