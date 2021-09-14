package castle.core.game.system

import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.common.system.IteratingIntervalSystem
import castle.core.game.component.MoveComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

class MoveSystem : IteratingIntervalSystem(MOVE_TICK, family) {
    private companion object {
        private val family: Family = Family.all(
            MoveComponent::class.java,
            PositionComponent::class.java,
            PhysicComponent::class.java
        ).get()
        private const val MOVE_TICK: Float = 1f / 30f
        private val right: Vector3 = Vector3(0f, 1f, 0f)
        private val left: Vector3 = Vector3(0f, -1f, 0f)
        private val zero: Vector3 = Vector3(0f, 0f, 0f)
        private val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
    }

    private val tempPosition: Vector3 = Vector3()
    private val tempDefaultFace: Vector3 = Vector3()
    private val tempTarget: Vector3 = Vector3()
    private val tempDirection: Vector3 = Vector3()
    private val tempOrientation: Quaternion = Quaternion()
    private val tempAngle: Quaternion = Quaternion()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveComponent = MoveComponent.mapper.get(entity)
        if (!moveComponent.enableMoving) {
            return
        }
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        val worldTransform = positionComponent.matrix4
        val unitPosition = worldTransform.getTranslation(tempPosition)
        val orientation = worldTransform.getRotation(tempOrientation)
        val target = tempTarget.set(moveComponent.target.x, unitPosition.y, moveComponent.target.y)
        val faceDirection = orientation.transform(tempDefaultFace.set(defaultFaceDirection))
        val direction = tempDirection.set(target).sub(unitPosition).nor()
        val angle = tempAngle.setFromCross(direction, faceDirection).angle
        val actualDistance = unitPosition.dst2(target)
        val needRotation = angle !in 0.0..10.0
        val needMove = actualDistance > moveComponent.distance
        when {
            needRotation -> {
                val speed = (if (tempAngle.y < 0) right else left).cpy().scl(moveComponent.speedAngular)
                physicComponent.physicInstance.body.angularVelocity = speed
                physicComponent.physicInstance.body.linearVelocity = zero
            }
            needMove -> {
                physicComponent.physicInstance.body.linearVelocity = direction.scl(moveComponent.speedLinear)
                physicComponent.physicInstance.body.angularVelocity = zero
            }
            else -> {
                physicComponent.physicInstance.body.linearVelocity = zero
                physicComponent.physicInstance.body.angularVelocity = zero
            }
        }
        moveComponent.isMoving = needRotation || needMove
    }
}