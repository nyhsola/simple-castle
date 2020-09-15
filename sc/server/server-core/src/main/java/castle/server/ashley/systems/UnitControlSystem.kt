package castle.server.ashley.systems

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.UnitComponent
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Vector3
import kotlin.math.acos

class UnitControlSystem : IteratingSystemAdapter(Family.all(UnitComponent::class.java, PositionComponent::class.java, PhysicComponent::class.java).get()) {

    companion object {
        val anglePrecision = 0.0..10.0
        val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)

        const val speedScalar: Float = 1f

        val right: Vector3 = Vector3(0f, 1f, 0f)
        val left: Vector3 = Vector3(0f, -1f, 0f)

        val noMove: Vector3 = Vector3(0f, 0f, 0f)
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)

        moveProceed(positionComponent, unitComponent, physicComponent)
    }

    private fun moveProceed(positionComponent: PositionComponent, unitComponent: UnitComponent, physicComponent: PhysicComponent) {
        val unitPosition = positionComponent.matrix4.getTranslation(Vector3())
        val targetDirection = unitComponent.aim.cpy().sub(unitPosition).nor().scl(speedScalar)

        val faceDirection = physicComponent.physicObject.body.orientation.transform(defaultFaceDirection.cpy())
        val diffAngle = getAngle(targetDirection, faceDirection)

        val previousAngle = unitComponent.previousAngle
        val rotateDirection = unitComponent.rotateDirection

        val angularVelocity: Vector3

        if (diffAngle !in anglePrecision) {
            if (diffAngle - previousAngle > 0) {
                unitComponent.rotateDirection = !rotateDirection
            }
            angularVelocity = if (unitComponent.rotateDirection) right.cpy() else left.cpy()
            unitComponent.previousAngle = diffAngle
        } else {
            angularVelocity = noMove.cpy()
        }

        physicComponent.physicObject.body.angularVelocity = angularVelocity
        physicComponent.physicObject.body.linearVelocity = faceDirection
    }


    private fun getAngle(a: Vector3, b: Vector3): Double {
        val norA = b.cpy()
        val norB = a.cpy()
        return Math.toDegrees(acos(norB.dot(norA).toDouble()))
    }
}