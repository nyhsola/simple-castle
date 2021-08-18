package castle.core.common.system

import castle.core.common.creator.GUIConfig
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.common.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.utils.Disposable

class ModelRenderSystem(
    guiConfig: GUIConfig,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(PositionComponent::class.java, RenderComponent::class.java).get()), Disposable {
    private val modelBatch: ModelBatch = guiConfig.modelBatch()

    override fun processEntity(entity: Entity, deltaTime: Float) {
    }

    override fun update(deltaTime: Float) {
        Gdx.gl.apply {
            glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            glClearColor(0.3f, 0.3f, 0.3f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            glEnable(GL20.GL_DEPTH_TEST)
        }
        modelBatch.begin(cameraService.currentCamera.camera)
        for (i in 0 until entities.size()) {
            val renderComponent = RenderComponent.mapper.get(entities[i])
            if (!renderComponent.hide) {
                modelBatch.render(renderComponent.modelInstance, cameraService.currentCamera.environment)
            }
        }
        modelBatch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
    }
}