package castle.server.ashley.systems.render

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.service.CameraService
import castle.server.ashley.systems.component.PositionComponent
import castle.server.ashley.systems.component.RenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.utils.Disposable

class ModelRenderSystem(guiCreator: GUICreator, private val cameraService: CameraService) :
    IteratingSystem(Family.all(PositionComponent::class.java, RenderComponent::class.java).get()), EntityListener, Disposable {
    private val modelBatch: ModelBatch = guiCreator.createModelBatch()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun entityAdded(entity: Entity) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val renderComponent = RenderComponent.mapper.get(entity)
        RenderComponent.link(positionComponent, renderComponent)
    }

    override fun update(deltaTime: Float) {
        Gdx.gl.apply {
            glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            glClearColor(0.3f, 0.3f, 0.3f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            glEnable(GL20.GL_DEPTH_TEST)
        }
        modelBatch.begin(cameraService.getCurrentCamera().camera)
        for (i in 0 until entities.size()) {
            val renderComponent = RenderComponent.mapper.get(entities[i])
            modelBatch.render(renderComponent.modelInstance, cameraService.getCurrentCamera().environment)
        }
        modelBatch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
    }
}