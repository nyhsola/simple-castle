package castle.core.behaviour

import castle.core.behaviour.component.GroundMeleeComponent
import castle.core.behaviour.controller.GroundMeleeUnitController
import castle.core.component.MapComponent
import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.component.render.AnimationRenderComponent
import castle.core.state.StateDelta
import castle.core.util.UnitUtils
import com.badlogic.ashley.core.Entity
import org.koin.core.annotation.Single

@Single
class GroundMeleeAttackBehaviour(
    private val controller: GroundMeleeUnitController
) {
    private val init = Init()
    private val moveRoute = MoveRoute()
    private val attackRoute = AttackRoute()
    private val attack = Attack()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity, delta: Float) {
            controller.init(entity)
            StateComponent.mapper.get(entity).state.changeState(moveRoute)
        }
    }

    private inner class MoveRoute : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            AnimationRenderComponent.mapper.get(entity).setAnimation("walk", 1.5f)
            MapComponent.mapper.get(entity).shouldSearchEntities = true
        }

        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val meleeComponent = GroundMeleeComponent.mapper.get(entity)
            val mapComponent = MapComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            controller.updateMovePath(entity)
            when {
                mapComponent.isUnitsAround -> {
                    val enemy = UnitUtils.findEnemies(unitComponent, mapComponent.inRadiusUnits).firstOrNull()
                    if (enemy != null) {
                        meleeComponent.targetEnemy = enemy
                        stateComponent.state.changeState(attackRoute)
                    }
                }

                unitComponent.isEnemiesInTouch -> {
                    meleeComponent.targetEnemy = unitComponent.inTouchEnemies.first()
                    stateComponent.state.changeState(attack)
                }
            }
        }

        override fun exit(entity: Entity) {
            MapComponent.mapper.get(entity).shouldSearchEntities = false
        }
    }

    private inner class AttackRoute : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            AnimationRenderComponent.mapper.get(entity).setAnimation("walk", 1.5f)
        }

        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val meleeComponent = GroundMeleeComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            val unitComponentEnemy = meleeComponent.targetEnemy!!
            controller.updateMoveTarget(entity)
            when {
                unitComponent.inTouchEnemies.contains(unitComponentEnemy) -> {
                    stateComponent.state.changeState(attack)
                }

                !unitComponent.inTouchEnemies.contains(unitComponentEnemy) && unitComponent.inTouchObjects.isNotEmpty() -> {
                    // re-init path to obj
                }

                unitComponentEnemy.isDead -> {
                    stateComponent.state.changeState(moveRoute)
                }
            }
        }
    }

    private inner class Attack : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            val meleeComponent = GroundMeleeComponent.mapper.get(entity)
            AnimationRenderComponent.mapper.get(entity).setAnimation(
                "attack",
                meleeComponent.attackSpeed,
                meleeComponent.attackTask
            )
        }

        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val meleeComponent = GroundMeleeComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            when {
                meleeComponent.targetEnemy!!.isDead -> {
                    stateComponent.state.changeState(moveRoute)
                }

                !unitComponent.inTouchEnemies.contains(meleeComponent.targetEnemy) -> {
                    stateComponent.state.changeState(moveRoute)
                }

                else -> controller.updateAttack(entity)
            }
        }
    }
}