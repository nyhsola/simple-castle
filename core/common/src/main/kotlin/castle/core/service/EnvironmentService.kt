package castle.core.service

import castle.core.builder.EnvironmentBuilder
import castle.core.component.render.ModelRenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

class EnvironmentService(
        private val environmentBuilder: EnvironmentBuilder,
        private val commonResources: CommonResources
) {
    val neutralUnits: MutableMap<String, Entity> = HashMap()

    fun init(engine: Engine) {
        commonResources.environment
                .map { environmentBuilder.build(it) }
                .flatten()
                .onEach { engine.addEntity(it) }
                .onEach { neutralUnits[ModelRenderComponent.mapper.get(it).nodeName] = it }
    }
}