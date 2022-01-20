package castle.core.game.builder

import castle.core.common.`object`.CommonEntity
import castle.core.common.builder.TemplateBuilder
import castle.core.common.component.AnimationComponent
import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.common.service.CommonResources
import castle.core.game.behaviour.Behaviours
import castle.core.game.component.AttackComponent
import castle.core.game.component.BehaviourComponent
import castle.core.game.component.HPComponent
import castle.core.game.component.MoveComponent
import castle.core.game.json.UnitJson
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3

class UnitBuilder(
    private val commonResources: CommonResources,
    private val templateBuilder: TemplateBuilder
) {
    fun build(unitJson: UnitJson, node: String): CommonEntity {
        val templates = commonResources.templates
        val templateJson = templates.getValue(unitJson.templateName)
        val buildEntity = templateBuilder.build(templateJson, node)
        return init(unitJson, buildEntity)
    }

    fun build(unitJson: UnitJson): CommonEntity {
        val templates = commonResources.templates
        val templateJson = templates.getValue(unitJson.templateName)
        val buildEntity = templateBuilder.build(templateJson)
        return init(unitJson, buildEntity)
    }

    private fun init(unitJson: UnitJson, unit: CommonEntity): CommonEntity {
        if (unitJson.hpSettings.enabled) {
            unit.add(initHPBar(unitJson, unit))
        }
        if (unitJson.moveSettings.enabled) {
            unit.add(MoveComponent(unitJson.moveSettings.speedLinear, unitJson.moveSettings.speedAngular))
        }
        if (unitJson.attackSettings.enabled) {
            val attackComponent = AttackComponent(
                unitJson.attackSettings.attackFrom..unitJson.attackSettings.attackTo,
                unitJson.attackSettings.attackSpeed,
                unitJson.attackSettings.range
            )
            unit.add(attackComponent)
        }
        if (unitJson.behaviourSettings.enabled) {
            unit.add(AnimationComponent(RenderComponent.mapper.get(unit)))
            unit.add(BehaviourComponent(unit, Behaviours.behaviors.getValue(unitJson.behaviourSettings.behaviour)))
        }
        return unit
    }

    private fun initHPBar(unitJson: UnitJson, entity: Entity): HPComponent {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        val texture = commonResources.textures.getValue(unitJson.hpSettings.textureName)
        val min = Vector3()
        val max = Vector3()
        physicComponent.physicInstance.body.getAabb(min, max)
        val height = max.sub(min).y * 1.1f
        val width = max.sub(min).z * 1.1f
        return HPComponent(unitJson.hpSettings.amount, width, height, texture, positionComponent.matrix4)
    }
}