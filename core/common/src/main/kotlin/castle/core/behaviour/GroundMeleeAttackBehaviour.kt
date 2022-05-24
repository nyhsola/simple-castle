package castle.core.behaviour

import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.component.render.AnimationRenderComponent
import castle.core.service.UnitService
import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity

class GroundMeleeAttackBehaviour(private val unitService: UnitService) {
    private val init = Init()
    private val moveRoute = MoveRoute()
    private val attackRoute = AttackRoute()
    private val attack = Attack()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            unitService.initPath(unitComponent)
            stateComponent.state.changeState(moveRoute)
        }
    }

    private inner class MoveRoute : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            AnimationRenderComponent.mapper.get(entity).setAnimation("walk", 1.5f)
            val unitComponent = UnitComponent.mapper.get(entity)
            unitService.initMelee(unitComponent)
        }

        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            unitService.updateMap(unitComponent)
            unitService.updateEnemies(unitComponent)
            unitService.updateMovePath(unitComponent)
            when {
                unitComponent.isEnemiesAround -> {
                    unitComponent.targetEnemy = unitComponent.inRadiusEnemies.first()
                    stateComponent.state.changeState(attackRoute)
                }
                unitComponent.isEnemiesInTouch -> {
                    unitComponent.targetEnemy = unitComponent.inTouchEnemies.first()
                    stateComponent.state.changeState(attack)
                }
            }
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
            unitService.updateMap(unitComponent)
            unitService.updateMoveTarget(unitComponent)
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
            unitService.updateMap(unitComponent)
            when {
                unitComponentEnemy.isDead -> {
                    stateComponent.state.changeState(moveRoute)
                }
                !unitComponent.inTouchEnemies.contains(unitComponent.targetEnemy) -> {
                    stateComponent.state.changeState(moveRoute)
                }
                else -> unitService.updateAttack(unitComponent, delta)
            }
        }
    }
}