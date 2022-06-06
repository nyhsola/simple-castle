package castle.core.builder

import castle.core.behaviour.Behaviours
import castle.core.component.*
import castle.core.component.render.AnimationRenderComponent
import castle.core.component.render.HPRenderComponent
import castle.core.component.render.ModelRenderComponent
import castle.core.json.UnitJson
import castle.core.service.CommonResources
import castle.core.service.EnvironmentService
import castle.core.service.GameResources
import com.badlogic.ashley.core.Entity

class UnitBuilder(
    private val commonResources: CommonResources,
    private val gameResources: GameResources,
    private val environmentService: EnvironmentService,
    private val templateBuilder: TemplateBuilder,
    private val behaviours: Behaviours
) {
    private companion object {
        private const val defaultHpTexture = "hp.png"
    }

    fun build(unitStr: String, spawn: String) : Entity {
        val unit = build(unitStr)
        val spawnUnit = environmentService.environmentObjects.getValue(spawn)
        PositionComponent.mapper.get(unit).setMatrix(PositionComponent.mapper.get(spawnUnit))
        return unit
    }

    fun build(unitStr: String): Entity {
        val unitJson = gameResources.units.getValue(unitStr)
        val buildEntity = templateBuilder.build(unitJson.templateName, unitJson.node)
        return buildInternal(unitJson, buildEntity)
    }

    private fun buildInternal(unitJson: UnitJson, unit: Entity): Entity {
        unit.add(
            UnitComponent(
                unit,
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
        unit.add(
            StateComponent(
                behaviours.behaviors.getValue(unitJson.behaviour),
                unit
            )
        )
        unit.add(MapComponent(unitJson.speedAngular == 0f))
        unit.add(AnimationRenderComponent(ModelRenderComponent.mapper.get(unit)))
        return unit
    }
}