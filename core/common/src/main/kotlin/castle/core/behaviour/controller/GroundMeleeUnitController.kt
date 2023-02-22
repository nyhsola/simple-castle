package castle.core.behaviour.controller

import castle.core.behaviour.component.GroundMeleeComponent
import castle.core.component.MapComponent
import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.service.EnvironmentService
import castle.core.service.MapService
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class GroundMeleeUnitController(
    private val mapService: MapService,
    private val environmentService: EnvironmentService
) {
    companion object {
        const val PATH_PARAM = "PATH"
    }

    enum class Distances(val distance: Float) {
        AT(0f)
    }

    private val right: Vector3 = Vector3(0f, 1f, 0f)
    private val left: Vector3 = Vector3(0f, -1f, 0f)
    private val zero: Vector3 = Vector3(0f, 0f, 0f)
    private val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)

    private val tempDefaultFace: Vector3 = Vector3()
    private val tempTarget: Vector3 = Vector3()
    private val tempPosition: Vector3 = Vector3()
    private val tempSpeed: Vector3 = Vector3()
    private val tempDirection: Vector3 = Vector3()
    private val tempOrientation: Quaternion = Quaternion()
    private val tempAngle: Quaternion = Quaternion()
    private val direction: Vector3 = Vector3()

    fun init(entity: Entity) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val unitJson = unitComponent.unitJson
        val meleeComponent = GroundMeleeComponent(
            unitJson.attackFrom..unitJson.attackTo,
            unitJson.attackSpeed,
            unitJson.speedLinear,
            unitJson.speedAngular
        )
        val path = unitComponent.params[PATH_PARAM] as List<*>
        val pathObjects = path.map { environmentService.environmentObjects[it as String] }
        val pathPositions = pathObjects.map { PositionComponent.mapper.get(it).matrix4.getTranslation(tempPosition).cpy() }
        val graphPath = mapService.getPath(pathPositions)
        meleeComponent.mainPath = graphPath
        meleeComponent.distance = Distances.AT.distance
        entity.add(meleeComponent)
    }

    fun updateMovePath(entity: Entity) {
        val mapComponent = MapComponent.mapper.get(entity)
        val meleeComponent = GroundMeleeComponent.mapper.get(entity)
        meleeComponent.targetMove.set(meleeComponent.nextArea.position)
        if (mapService.inRadius(mapComponent.currentArea, meleeComponent.nextArea)) {
            meleeComponent.nextPath = meleeComponent.nextPath + 1
        }
        updateMoveParams(entity, meleeComponent)
        updateMoveInternal(entity, meleeComponent)
    }

    fun updateMoveTarget(entity: Entity) {
        val meleeComponent = GroundMeleeComponent.mapper.get(entity)
        val targetEntity = meleeComponent.targetEnemy!!.owner
        val movingTarget = PositionComponent.mapper.get(targetEntity).matrix4.getTranslation(tempPosition)
        meleeComponent.targetMove.set(movingTarget.x, movingTarget.z)
        updateMoveParams(entity, meleeComponent)
        updateMoveInternal(entity, meleeComponent)
    }

    fun updateAttack(entity: Entity) {
        val meleeComponent = GroundMeleeComponent.mapper.get(entity)
        val enemyTarget = PositionComponent.mapper.get(meleeComponent.targetEnemy!!.owner).matrix4.getTranslation(tempPosition)
        meleeComponent.targetMove.set(enemyTarget.x, enemyTarget.z)
        updateMoveParams(entity, meleeComponent)
    }

    private fun updateMoveParams(owner: Entity, meleeComponent: GroundMeleeComponent) {
        val matrix4 = PositionComponent.mapper.get(owner).matrix4
        val unitPosition = matrix4.getTranslation(tempPosition)
        val orientation = matrix4.getRotation(tempOrientation)
        val targetLevel = tempTarget.set(meleeComponent.targetMove.x, unitPosition.y, meleeComponent.targetMove.y)
        val faceDirection = orientation.transform(tempDefaultFace.set(defaultFaceDirection))
        val directionNew = tempDirection.set(targetLevel).sub(unitPosition).nor()
        val angle = tempAngle.setFromCross(directionNew, faceDirection).angle
        val actualDistance = unitPosition.dst2(targetLevel)
        direction.set(directionNew)
        meleeComponent.needRotation = angle !in 0.0..10.0
        meleeComponent.needMove = actualDistance > meleeComponent.distance
    }

    private fun updateMoveInternal(owner: Entity, meleeComponent: GroundMeleeComponent) {
        val physicComponent = PhysicComponent.mapper.get(owner)
        when {
            meleeComponent.needRotation -> {
                tempSpeed.set(if (tempAngle.y < 0) right else left).scl(meleeComponent.speedAngular)
                physicComponent.body.angularVelocity = tempSpeed
                physicComponent.body.linearVelocity = zero
            }

            meleeComponent.needMove -> {
                physicComponent.body.linearVelocity = direction.scl(meleeComponent.speedLinear)
                physicComponent.body.angularVelocity = zero
            }

            else -> {
                physicComponent.body.linearVelocity = zero
                physicComponent.body.angularVelocity = zero
            }
        }
    }

}