package castle.core.behaviour

import castle.core.behaviour.controller.GroundUnitController
import castle.core.component.MapComponent
import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.component.render.AnimationRenderComponent
import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity

class GroundMeleeAttackBehaviour(
    private val controller: GroundUnitController
) {
    private val init = Init()
    private val moveRoute = MoveRoute()
    private val attackRoute = AttackRoute()
    private val attack = Attack()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            controller.initPath(unitComponent)
            stateComponent.state.changeState(moveRoute)
        }
    }

    private inner class MoveRoute : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            AnimationRenderComponent.mapper.get(entity).setAnimation("walk", 1.5f)
            MapComponent.mapper.get(entity).shouldSearchEntities = true
            controller.initMelee(UnitComponent.mapper.get(entity))
        }

        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val mapComponent = MapComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            controller.updateMovePath(unitComponent)
            when {
                mapComponent.isEnemiesAround -> {
                    val enemy = mapComponent.inRadiusEnemies.firstOrNull() { it != unitComponent }
                    if (enemy != null) {
                        unitComponent.targetEnemy = enemy
                        stateComponent.state.changeState(attackRoute)
                    }
                }
                unitComponent.isEnemiesInTouch -> {
                    unitComponent.targetEnemy = unitComponent.inTouchEnemies.first()
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
            val stateComponent = StateComponent.mapper.get(entity)
            val unitComponentEnemy = unitComponent.targetEnemy!!
            controller.updateMoveTarget(unitComponent)
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
            AnimationRenderComponent.mapper.get(entity).setAnimation("attack", 1.5f)
        }

        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            val unitComponentEnemy = unitComponent.targetEnemy!!
            when {
                unitComponentEnemy.isDead -> {
                    stateComponent.state.changeState(moveRoute)
                }
                !unitComponent.inTouchEnemies.contains(unitComponent.targetEnemy) -> {
                    stateComponent.state.changeState(moveRoute)
                }
                else -> controller.updateAttack(unitComponent, delta)
            }
        }
    }
}