package castle.core.service

import castle.core.builder.EnvironmentBuilder
import castle.core.component.render.ModelRenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Disposable

class EnvironmentService(
    private val engine: Engine,
    private val environmentBuilder: EnvironmentBuilder
) : Disposable {
    val environmentObjects: MutableMap<String, Entity> = HashMap()

    fun init() {
        environmentBuilder.build()
            .onEach { engine.addEntity(it) }
            .onEach { environmentObjects[ModelRenderComponent.mapper.get(it).nodeName] = it }
    }

    override fun dispose() {
        environmentObjects.forEach { engine.removeEntity(it.value) }
    }
}