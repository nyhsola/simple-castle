package castle.core.system.render

import castle.core.component.PositionComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.ModelBatch
import org.koin.core.annotation.Single

@Single
class ModelRenderSystem(
    private val modelBatch: ModelBatch,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(PositionComponent::class.java, ModelRenderComponent::class.java).get()) {
    override fun update(deltaTime: Float) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        modelBatch.begin(cameraService.currentCamera.camera)
        super.update(deltaTime)
        modelBatch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val modelRenderComponent = ModelRenderComponent.mapper.get(entity)
        if (!modelRenderComponent.hide) {
            modelBatch.render(modelRenderComponent.modelInstance, cameraService.currentCamera.environment)
        }
    }
}