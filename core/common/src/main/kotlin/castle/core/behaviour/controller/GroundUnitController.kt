package castle.core.behaviour.controller

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.service.EnvironmentService
import castle.core.service.MapService
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class GroundUnitController(
    private val mapService: MapService,
    private val environmentService: EnvironmentService
) {
    companion object {
        enum class Distances(val distance: Float) {
            AT(0f)
        }

        private val right: Vector3 = Vector3(0f, 1f, 0f)
        private val left: Vector3 = Vector3(0f, -1f, 0f)
        private val zero: Vector3 = Vector3(0f, 0f, 0f)
        private val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
    }

    private val temp: Vector3 = Vector3()
    private val tempDefaultFace: Vector3 = Vector3()
    private val tempTarget: Vector3 = Vector3()
    private val tempPosition: Vector3 = Vector3()
    private val tempSpeed: Vector3 = Vector3()
    private val tempDirection: Vector3 = Vector3()
    private val tempOrientation: Quaternion = Quaternion()
    private val tempAngle: Quaternion = Quaternion()
    private val direction: Vector3 = Vector3()

    fun initPath(unitComponent: UnitComponent) {
        val pathObjects = unitComponent.path.map { environmentService.environmentObjects[it] }
        val pathPositions = pathObjects.map { PositionComponent.mapper.get(it).matrix4.getTranslation(temp).cpy() }
        val graphPath = mapService.getPath(pathPositions)
        unitComponent.mainPath = graphPath
    }

    fun initMelee(unitComponent: UnitComponent) {
        unitComponent.distance = Distances.AT.distance
    }

    fun updateMovePath(unitComponent: UnitComponent) {
        unitComponent.targetMove.set(unitComponent.nextArea.position)
        if (mapService.inRadius(unitComponent.currentArea, unitComponent.nextArea)) {
            unitComponent.nextPath = unitComponent.nextPath + 1
        }
        updateMoveParams(unitComponent)
        updateMoveInternal(unitComponent)
    }

    fun updateMoveTarget(unitComponent: UnitComponent) {
        val targetEntity = unitComponent.targetEnemy!!.owner
        val movingTarget = PositionComponent.mapper.get(targetEntity).matrix4.getTranslation(temp)
        unitComponent.targetMove.set(movingTarget.x, movingTarget.z)
        updateMoveParams(unitComponent)
        updateMoveInternal(unitComponent)
    }

    fun updateAttack(unitComponent: UnitComponent, deltaTime: Float) {
        val enemyTarget = PositionComponent.mapper.get(unitComponent.targetEnemy!!.owner).matrix4.getTranslation(temp)
        unitComponent.targetMove.set(enemyTarget.x, enemyTarget.z)
        unitComponent.attackTask.update(deltaTime)
        updateMoveParams(unitComponent)
    }

    private fun updateMoveParams(unitComponent: UnitComponent) {
        val matrix4 = PositionComponent.mapper.get(unitComponent.owner).matrix4
        val unitPosition = matrix4.getTranslation(tempPosition)
        val orientation = matrix4.getRotation(tempOrientation)
        val targetLevel = tempTarget.set(unitComponent.targetMove.x, unitPosition.y, unitComponent.targetMove.y)
        val faceDirection = orientation.transform(tempDefaultFace.set(defaultFaceDirection))
        val directionNew = tempDirection.set(targetLevel).sub(unitPosition).nor()
        val angle = tempAngle.setFromCross(directionNew, faceDirection).angle
        val actualDistance = unitPosition.dst2(targetLevel)
        direction.set(directionNew)
        unitComponent.needRotation = angle !in 0.0..10.0
        unitComponent.needMove = actualDistance > unitComponent.distance
    }

    private fun updateMoveInternal(unitComponent: UnitComponent) {
        val physicComponent = PhysicComponent.mapper.get(unitComponent.owner)
        when {
            unitComponent.needRotation -> {
                tempSpeed.set(if (tempAngle.y < 0) right else left).scl(unitComponent.speedAngular)
                physicComponent.body.angularVelocity = tempSpeed
                physicComponent.body.linearVelocity = zero
            }
            unitComponent.needMove -> {
                physicComponent.body.linearVelocity = direction.scl(unitComponent.speedLinear)
                physicComponent.body.angularVelocity = zero
            }
            else -> {
                physicComponent.body.linearVelocity = zero
                physicComponent.body.angularVelocity = zero
            }
        }
    }

}