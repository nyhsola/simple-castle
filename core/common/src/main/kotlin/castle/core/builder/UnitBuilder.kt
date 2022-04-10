package castle.core.builder

import castle.core.`object`.CommonEntity
import castle.core.behaviour.Behaviours
import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.AnimationRenderComponent
import castle.core.component.render.HPRenderComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.json.UnitJson
import castle.core.service.CommonResources

class UnitBuilder(
        private val commonResources: CommonResources,
        private val templateBuilder: TemplateBuilder
) {
    fun build(unitJson: UnitJson): CommonEntity {
        val templates = commonResources.templates
        val templateJson = templates.getValue(unitJson.templateName)
        val buildEntity = templateBuilder.build(templateJson, unitJson.node)
        return init(unitJson, buildEntity)
    }

    private fun init(unitJson: UnitJson, unit: CommonEntity): CommonEntity {
        if (unitJson.hpSettings.enabled) {
            unit.add(HPRenderComponent(
                    commonResources.textures.getValue(unitJson.hpSettings.textureName),
                    PositionComponent.mapper.get(unit).matrix4,
                    PhysicComponent.mapper.get(unit).body
            ))
        }
        if (unitJson.unitSettings.enabled) {
            unit.add(UnitComponent(
                    unitJson.unitSettings.unitType,
                    unitJson.unitSettings.amount,
                    unitJson.unitSettings.speedLinear,
                    unitJson.unitSettings.speedAngular,
                    unitJson.unitSettings.attackFrom..unitJson.unitSettings.attackTo,
                    unitJson.unitSettings.attackSpeed,
                    unitJson.unitSettings.scanRange,
                    unit,
                    Behaviours.behaviors.getValue(unitJson.unitSettings.behaviour)))
        }
        if (unitJson.unitSettings.behaviour != "none") {
            unit.add(AnimationRenderComponent(ModelRenderComponent.mapper.get(unit)))
        }
        return unit
    }
}