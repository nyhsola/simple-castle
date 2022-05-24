package castle.core.service

import castle.core.builder.EnvironmentBuilder
import castle.core.component.render.ModelRenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

class EnvironmentService(
    private val environmentBuilder: EnvironmentBuilder
) {
    val environmentObjects: MutableMap<String, Entity> = HashMap()

    fun init(engine: Engine) {
        environmentBuilder.buildEnvironment()
            .onEach { engine.addEntity(it) }
            .onEach { environmentObjects[ModelRenderComponent.mapper.get(it).nodeName] = it }
    }
}