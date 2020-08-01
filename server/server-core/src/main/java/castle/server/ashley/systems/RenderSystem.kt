package castle.server.ashley.systems

import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.RenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch

class RenderSystem(private val camera: Camera,
                   private val environment: Environment,
                   private val modelBatch: ModelBatch) : IteratingSystemAdapter(Family.all(PositionComponent::class.java, RenderComponent::class.java).get()), EntityListener {

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun entityAdded(entity: Entity) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val renderComponent = RenderComponent.mapper.get(entity)
        RenderComponent.link(positionComponent, renderComponent)
    }

    override fun render(delta: Float) {
        Gdx.gl.apply {
            glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            glClearColor(0.3f, 0.3f, 0.3f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        }
        modelBatch.begin(camera)
        for (i in 0 until entities.size()) {
            val renderComponent = RenderComponent.mapper.get(entities[i])
            if (!renderComponent.hide) modelBatch.render(renderComponent.modelInstance, environment)
        }
        modelBatch.end()
    }

}