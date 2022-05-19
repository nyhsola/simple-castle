package castle.core.builder

import castle.core.behaviour.Behaviours
import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.AnimationRenderComponent
import castle.core.component.render.HPRenderComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.json.UnitJson
import castle.core.service.CommonResources
import com.badlogic.ashley.core.Entity

class UnitBuilder(
    private val commonResources: CommonResources,
    private val templateBuilder: TemplateBuilder,
    private val behaviours: Behaviours
) {
    private companion object {
        private const val defaultHpTexture = "hp.png"
    }

    fun build(unitJson: UnitJson): Entity {
        val templates = commonResources.templates
        val templateJson = templates.getValue(unitJson.templateName)
        val buildEntity = templateBuilder.build(templateJson, unitJson.node)
        return init(unitJson, buildEntity)
    }

    private fun init(unitJson: UnitJson, unit: Entity): Entity {
        unit.add(
            UnitComponent(
                behaviours.behaviors.getValue(unitJson.behaviour),
                unit,
                unitJson.unitType,
                unitJson.amount,
                unitJson.attackFrom..unitJson.attackTo,
                unitJson.attackSpeed,
                unitJson.visibilityRange,
                unitJson.speedLinear,
                unitJson.speedAngular
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