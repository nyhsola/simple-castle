package castle.core.behaviour

import castle.core.component.UnitComponent
import castle.core.component.render.AnimationRenderComponent
import castle.core.`object`.CommonEntity
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

enum class GroundAttackBehaviour : State<Entity> {
    INIT {
        override fun enter(entity: Entity) {
        }

        override fun update(entity: Entity) {
            UnitComponent.mapper.get(entity).state.changeState(MOVE_ROUTE)
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    },
    MOVE_ROUTE {
        override fun enter(entity: Entity) {
            AnimationRenderComponent.mapper.get(entity).setAnimation("walk", 1.5f)
            UnitComponent.mapper.get(entity).goByPath()
        }

        override fun update(entity: Entity) {
            val unitComponent = UnitComponent.mapper.get(entity)
            if (unitComponent.nearEnemies.isNotEmpty()) {
                unitComponent.lockEnemy()
                unitComponent.state.changeState(ATTACK_ROUTE)
            }
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    },
    ATTACK_ROUTE {
        override fun enter(entity: Entity) {
            AnimationRenderComponent.mapper.get(entity).setAnimation("walk", 1.5f)
            UnitComponent.mapper.get(entity).goByTarget()
        }

        override fun update(entity: Entity) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val unitComponentEnemy = UnitComponent.mapper.get(unitComponent.targetEnemy)
            if (!unitComponent.isMoving && (unitComponentEnemy == null || !unitComponentEnemy.isMoving)) {
                unitComponent.state.changeState(ATTACK)
            }
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    },
    ATTACK {
        override fun enter(entity: Entity) {
            AnimationRenderComponent.mapper.get(entity).setAnimation("attack", 1.5f)
            UnitComponent.mapper.get(entity).doNothing()
        }

        override fun update(entity: Entity) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val enemy = unitComponent.targetEnemy as CommonEntity
            val unitComponentEnemy = UnitComponent.mapper.get(unitComponent.targetEnemy)
            when {
                enemy.removed -> {
                    unitComponent.enableAttacking = false
                    unitComponent.state.changeState(MOVE_ROUTE)
                }
                unitComponent.isMoving || (unitComponentEnemy != null && unitComponentEnemy.isMoving) -> {
                    unitComponent.goByTarget()
                    unitComponent.state.changeState(ATTACK_ROUTE)
                }
                else -> {
                    unitComponent.enableAttacking = true
                }
            }
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    };
}