package castle.core.service

import castle.core.`object`.CommonEntity
import castle.core.builder.EnvironmentBuilder
import castle.core.component.render.ModelRenderComponent
import com.badlogic.ashley.core.Engine

class EnvironmentInitService(
        private val environmentBuilder: EnvironmentBuilder,
        private val commonResources: CommonResources
) {
    val neutralUnits: MutableMap<String, CommonEntity> = HashMap()

    fun init(engine: Engine) {
        commonResources.environment
                .map { environmentBuilder.build(it) }
                .flatten()
                .onEach { it.add(engine) }
                .onEach { neutralUnits[ModelRenderComponent.mapper.get(it).nodeName] = it }
    }
}