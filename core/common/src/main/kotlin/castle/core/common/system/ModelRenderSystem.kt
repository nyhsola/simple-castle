package castle.core.common.system

import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.common.config.GUIConfig
import castle.core.common.service.CameraService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.ModelBatch

class ModelRenderSystem(
    guiConfig: GUIConfig,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(PositionComponent::class.java, RenderComponent::class.java).get()) {
    private val modelBatch: ModelBatch = guiConfig.modelBatch

    override fun update(deltaTime: Float) {
        Gdx.gl.apply {
            glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            glClearColor(0.3f, 0.3f, 0.3f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            glEnable(GL20.GL_DEPTH_TEST)
        }
        modelBatch.begin(cameraService.currentCamera.camera)
        super.update(deltaTime)
        modelBatch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = RenderComponent.mapper.get(entity)
        if (!renderComponent.hide) {
            modelBatch.render(renderComponent.modelInstance, cameraService.currentCamera.environment)
        }
    }
}