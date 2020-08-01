package castle.server.ashley.systems

import castle.server.ashley.component.RenderComponent
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch

class RenderSystem(private val camera: Camera,
                   private val environment: Environment,
                   private val modelBatch: ModelBatch) : IteratingSystemAdapter(Family.all(RenderComponent::class.java).get()) {
    companion object {
        val renderMapper: ComponentMapper<RenderComponent> = ComponentMapper.getFor(RenderComponent::class.java)
    }

    override fun render(delta: Float) {
        Gdx.gl.apply {
            glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            glClearColor(0.3f, 0.3f, 0.3f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        }
        modelBatch.begin(camera)
        for (i in 0 until entities.size()) {
            val renderComponent = renderMapper.get(entities[i])
            if (!renderComponent.hide) modelBatch.render(renderComponent.modelInstance, environment)
        }
        modelBatch.end()
    }

}