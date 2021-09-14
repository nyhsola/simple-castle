package castle.core.game.json

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.AnimationComponent
import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.game.behaviour.Behaviours
import castle.core.game.component.AttackComponent
import castle.core.game.component.BehaviourComponent
import castle.core.game.component.HPComponent
import castle.core.game.component.MoveComponent
import castle.core.game.json.settings.AttackSettings
import castle.core.game.json.settings.BehaviourSettings
import castle.core.game.json.settings.HPSettings
import castle.core.game.json.settings.MoveSettings
import castle.core.game.service.GameResourceService
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3

data class UnitJson(
    val unitName: String = "",
    val templateName: String = "",
    val attackSettings: AttackSettings = AttackSettings(false),
    val moveSettings: MoveSettings = MoveSettings(false),
    val hpSettings: HPSettings = HPSettings(false),
    val behaviourSettings: BehaviourSettings = BehaviourSettings(false)
) {
    fun buildUnit(resourceService: GameResourceService, node: String): CommonEntity {
        val model = resourceService.model
        val templates = resourceService.templates
        val templateJson = templates.getValue(templateName)
        val buildEntity = templateJson.buildUnit(model, node)
        return init(resourceService, buildEntity)
    }

    fun buildUnit(resourceService: GameResourceService): CommonEntity {
        val model = resourceService.model
        val templates = resourceService.templates
        val templateJson = templates.getValue(templateName)
        val buildEntity = templateJson.buildUnit(model)
        return init(resourceService, buildEntity)
    }

    private fun init(gameResourceService: GameResourceService, unit: CommonEntity): CommonEntity {
        if (hpSettings.enabled) {
            unit.add(initHPBar(gameResourceService, unit))
        }
        if (moveSettings.enabled) {
            unit.add(MoveComponent(moveSettings.speedLinear, moveSettings.speedAngular))
        }
        if (attackSettings.enabled) {
            val attackComponent = AttackComponent(
                attackSettings.attackFrom..attackSettings.attackTo,
                attackSettings.attackSpeed,
                attackSettings.range
            )
            unit.add(attackComponent)
        }
        if (behaviourSettings.enabled) {
            unit.add(AnimationComponent(RenderComponent.mapper.get(unit)))
            unit.add(BehaviourComponent(unit, Behaviours.behaviors.getValue(behaviourSettings.behaviour)))
        }
        return unit
    }

    private fun initHPBar(gameResourceService: GameResourceService, entity: Entity): HPComponent {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        val texture = gameResourceService.textures.getValue(hpSettings.textureName)
        val min = Vector3()
        val max = Vector3()
        physicComponent.physicInstance.body.getAabb(min, max)
        val height = max.sub(min).y * 1.1f
        val width = max.sub(min).z * 1.1f
        return HPComponent(hpSettings.amount, width, height, texture, positionComponent.matrix4)
    }
}