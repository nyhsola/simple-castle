package castle.core.game.behaviour

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.AnimationComponent
import castle.core.common.component.PositionComponent
import castle.core.game.component.*
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.math.Vector3

enum class AttackBehaviour : State<Entity> {
    INIT {
        override fun enter(entity: Entity) {
        }

        override fun update(entity: Entity) {
            BehaviourComponent.mapper.get(entity).state.changeState(MOVE_ROUTE)
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    },
    MOVE_ROUTE {
        override fun enter(entity: Entity) {
            val animationComponent = AnimationComponent.mapper.get(entity)
            animationComponent.setAnimation("walk", 1.5f)
            val moveComponent = MoveComponent.mapper.get(entity)
            moveComponent.enableMoving = true
            moveComponent.distance = MoveComponent.Companion.Distances.AT.distance
        }

        override fun update(entity: Entity) {
            val pathComponent = PathComponent.mapper.get(entity)
            val moveComponent = MoveComponent.mapper.get(entity)
            val attackComponent = AttackComponent.mapper.get(entity)

            if (attackComponent.nearObjects.isEmpty()) {
                moveComponent.target.set(pathComponent.graphPath[pathComponent.currentPosition].position)
            } else {
                val enemies = getEnemies(entity, attackComponent.nearObjects)
                if (enemies.isNotEmpty()) {
                    BehaviourComponent.mapper.get(entity).state.changeState(ATTACK_ROUTE)
                    AttackComponent.mapper.get(entity).target = enemies[0]
                }
            }
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    },
    ATTACK_ROUTE {
        private val temp: Vector3 = Vector3()

        override fun enter(entity: Entity) {
            val animationComponent = AnimationComponent.mapper.get(entity)
            animationComponent.setAnimation("walk", 1.5f)
            val moveComponent = MoveComponent.mapper.get(entity)
            moveComponent.distance = MoveComponent.Companion.Distances.MELEE.distance
        }

        override fun update(entity: Entity) {
            val moveComponent = MoveComponent.mapper.get(entity)
            val attackComponent = AttackComponent.mapper.get(entity)
            val targetPosition = PositionComponent.mapper.get(attackComponent.target).matrix4.getTranslation(temp)
            moveComponent.target.set(targetPosition.x, targetPosition.z)
            if (!moveComponent.isMoving) {
                BehaviourComponent.mapper.get(entity).state.changeState(ATTACK)
            }
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    },
    ATTACK {
        private val temp: Vector3 = Vector3()

        override fun enter(entity: Entity) {
            val animationComponent = AnimationComponent.mapper.get(entity)
            animationComponent.setAnimation("attack", 3f)
        }

        override fun update(entity: Entity) {
            val moveComponent = MoveComponent.mapper.get(entity)
            val attackComponent = AttackComponent.mapper.get(entity)
            val enemy = attackComponent.target as CommonEntity

            if (enemy.removed) {
                attackComponent.enableAttacking = false
                BehaviourComponent.mapper.get(entity).state.changeState(MOVE_ROUTE)
                return
            }

            val targetPosition = PositionComponent.mapper.get(attackComponent.target).matrix4.getTranslation(temp)
            moveComponent.target.set(targetPosition.x, targetPosition.z)

            if (moveComponent.isMoving) {
                attackComponent.enableAttacking = false
                BehaviourComponent.mapper.get(entity).state.changeState(ATTACK_ROUTE)
                return
            }

            attackComponent.enableAttacking = true
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    };

    fun getEnemies(me: Entity, others: List<Entity>): List<Entity> {
        val playerComponent = PlayerComponent.mapper.get(me)
        return others
            .filter { PlayerComponent.mapper.has(it) }
            .filter {
                val component = PlayerComponent.mapper.get(it)
                playerComponent.playerName != component.playerName
            }
    }
}