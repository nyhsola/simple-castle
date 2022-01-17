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
            val behaviourComponent = BehaviourComponent.mapper.get(entity)
            behaviourComponent.state.changeState(MOVE_ROUTE)
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
            val moveComponent = MoveComponent.mapper.get(entity)
            animationComponent.setAnimation("walk", 1.5f)
            moveComponent.enableMoving = true
            moveComponent.distance = MoveComponent.Companion.Distances.AT.distance
        }

        override fun update(entity: Entity) {
            val pathComponent = PathComponent.mapper.get(entity)
            val moveComponent = MoveComponent.mapper.get(entity)
            val attackComponent = AttackComponent.mapper.get(entity)
            val behaviourComponent = BehaviourComponent.mapper.get(entity)
            val nearObjects = attackComponent.nearObjects
            val enemies = getEnemies(entity, nearObjects)
            when {
                enemies.isEmpty() -> {
                    val nextPosition = pathComponent.nextPosition
                    val nextArea = pathComponent.graphPath[nextPosition]
                    moveComponent.target.set(nextArea.position)
                }
                enemies.isNotEmpty() -> {
                    behaviourComponent.state.changeState(ATTACK_ROUTE)
                    attackComponent.target = enemies[0]
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
            val moveComponent = MoveComponent.mapper.get(entity)
            animationComponent.setAnimation("walk", 1.5f)
            moveComponent.distance = MoveComponent.Companion.Distances.MELEE.distance
        }

        override fun update(entity: Entity) {
            val moveComponent = MoveComponent.mapper.get(entity)
            val attackComponent = AttackComponent.mapper.get(entity)
            val behaviourComponent = BehaviourComponent.mapper.get(entity)

            val moveComponentEnemy = MoveComponent.mapper.get(attackComponent.target)
            val positionComponentEnemy = PositionComponent.mapper.get(attackComponent.target)
            val targetPositionEnemy = positionComponentEnemy.matrix4.getTranslation(temp)

            moveComponent.target.set(targetPositionEnemy.x, targetPositionEnemy.z)
            if (!moveComponent.isMoving && !moveComponentEnemy.isMoving) {
                behaviourComponent.state.changeState(ATTACK)
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
            val behaviourComponent = BehaviourComponent.mapper.get(entity)
            val enemy = attackComponent.target as CommonEntity
            val positionComponentEnemy = PositionComponent.mapper.get(enemy)
            val moveComponentEnemy = MoveComponent.mapper.get(attackComponent.target)
            val targetPositionEnemy = positionComponentEnemy.matrix4.getTranslation(temp)
            when {
                enemy.removed -> {
                    attackComponent.enableAttacking = false
                    behaviourComponent.state.changeState(MOVE_ROUTE)
                }
                moveComponent.isMoving || moveComponentEnemy.isMoving -> {
                    attackComponent.enableAttacking = false
                    moveComponent.target.set(targetPositionEnemy.x, targetPositionEnemy.z)
                    behaviourComponent.state.changeState(ATTACK_ROUTE)
                }
                else -> {
                    attackComponent.enableAttacking = true
                }
            }
        }

        override fun exit(entity: Entity) {
        }

        override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
            return false
        }
    };

    fun getEnemies(me: Entity, others: List<Entity>): List<Entity> {
        val sideComponent = SideComponent.mapper.get(me)
        return others
            .filter { SideComponent.mapper.has(it) }
            .filter {
                val component = SideComponent.mapper.get(it)
                sideComponent.side != component.side
            }
    }
}