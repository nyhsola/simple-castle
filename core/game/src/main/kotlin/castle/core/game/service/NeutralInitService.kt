package castle.core.game.service

import castle.core.common.`object`.CommonEntity
import castle.core.common.builder.EnvironmentBuilder
import castle.core.common.component.RenderComponent
import castle.core.common.service.CommonResources
import com.badlogic.ashley.core.Engine

class NeutralInitService(
    private val environmentBuilder: EnvironmentBuilder,
    private val commonResources: CommonResources
) {
    val neutralUnits: MutableMap<String, CommonEntity> = HashMap()

    fun init(engine: Engine) {
        commonResources.environment
            .map { environmentBuilder.build(it) }
            .flatten()
            .onEach { it.add(engine) }
            .onEach { neutralUnits[RenderComponent.mapper.get(it).nodeName] = it }
    }
}