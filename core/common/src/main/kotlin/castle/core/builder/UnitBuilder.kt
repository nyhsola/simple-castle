package castle.core.builder

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.AnimationRenderComponent
import castle.core.component.render.HPRenderComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.json.UnitJson
import castle.core.`object`.CommonEntity
import castle.core.service.CommonResources

class UnitBuilder(
    private val commonResources: CommonResources,
    private val templateBuilder: TemplateBuilder
) {
    private companion object {
        private const val defaultHpTexture = "hp.png"
    }

    fun build(unitJson: UnitJson): CommonEntity {
        val templates = commonResources.templates
        val templateJson = templates.getValue(unitJson.templateName)
        val buildEntity = templateBuilder.build(templateJson, unitJson.node)
        return init(unitJson, buildEntity)
    }

    private fun init(unitJson: UnitJson, unit: CommonEntity): CommonEntity {
        unit.add(
            UnitComponent(
                unit,
                unitJson.behaviour,
                unitJson.unitType,
                unitJson.amount,
                unitJson.speedLinear,
                unitJson.speedAngular,
                unitJson.attackFrom..unitJson.attackTo,
                unitJson.attackSpeed,
                unitJson.scanRange
            )
        )
        unit.add(
            HPRenderComponent(
                commonResources.textures.getValue(defaultHpTexture),
                PositionComponent.mapper.get(unit).matrix4,
                PhysicComponent.mapper.get(unit).body
            )
        )
        if (unitJson.behaviour != "none") {
            unit.add(AnimationRenderComponent(ModelRenderComponent.mapper.get(unit)))
        }
        return unit
    }
}