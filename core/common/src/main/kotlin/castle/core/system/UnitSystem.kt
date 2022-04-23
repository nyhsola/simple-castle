package castle.core.system

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.LineRenderComponent
import castle.core.event.EventQueue
import castle.core.`object`.CommonEntity
import castle.core.service.EnvironmentInitService
import castle.core.service.MapService
import castle.core.system.abs.IteratingIntervalSystem
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

class UnitSystem(
        private val eventQueue: EventQueue,
        private val environmentInitService: EnvironmentInitService,
        private val mapService: MapService
) : IteratingIntervalSystem(UNIT_TICK, family), EntityListener {
    companion object {
        const val DEBUG_ENABLE = "DEBUG_PATH_ENABLE"
        private val family: Family = Family.all(
                UnitComponent::class.java,
                PositionComponent::class.java,
                PhysicComponent::class.java
        ).get()
        private const val UNIT_TICK: Float = 1f / 10f
        private val right: Vector3 = Vector3(0f, 1f, 0f)
        private val left: Vector3 = Vector3(0f, -1f, 0f)
        private val zero: Vector3 = Vector3(0f, 0f, 0f)
        private val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
    }

    private var debugEnabled: Boolean = false
    private val lines: MutableList<CommonEntity> = ArrayList()

    private val tempPosition: Vector3 = Vector3()
    private val tempDefaultFace: Vector3 = Vector3()
    private val tempTarget: Vector3 = Vector3()
    private val tempSpeed: Vector3 = Vector3()
    private val tempDirection: Vector3 = Vector3()
    private val tempOrientation: Quaternion = Quaternion()
    private val tempAngle: Quaternion = Quaternion()
    private val temp: Vector3 = Vector3()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val pathPositions = unitComponent.path
                .map { environmentInitService.neutralUnits[it] }
                .map { PositionComponent.mapper.get(it).matrix4.getTranslation(tempPosition).cpy() }
        unitComponent.graphPath = mapService.getPath(pathPositions)
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun update(deltaTime: Float) {
        proceedEvents()
        mapService.proceedEvents(engine)
        super.update(deltaTime)
    }

    override fun tickUpdate(deltaTime: Float) {
        GdxAI.getTimepiece().update(deltaTime)
        MessageManager.getInstance().update()
        super.tickUpdate(deltaTime)
        mapService.updateMap()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        mapService.updateEntity(entity)

        val unitComponent = UnitComponent.mapper.get(entity)
        unitComponent.state.update()

        if (unitComponent.unitType != "building") {
            updateEnemies(entity, deltaTime)
            updatePath(entity)
            updateTarget(entity)
            updateMove(entity)
        }

        checkDeath(entity)
    }

    private fun proceedEvents() {
        eventQueue.proceed { eventContext ->
            when (eventContext.eventType) {
                DEBUG_ENABLE -> {
                    debugEnabled = !debugEnabled
                    if (debugEnabled) {
                        engine.getEntitiesFor(family).onEach { createDebugLines(it, lines) }
                        lines.onEach { engine.addEntity(it) }
                    } else {
                        lines.onEach { engine.removeEntity(it) }
                        lines.clear()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun checkDeath(entity: Entity) {
        val unitComponent = UnitComponent.mapper.get(entity)
        if (unitComponent.currentAmount <= 0) {
            mapService.removeFromMap(entity)
            val commonEntity = entity as CommonEntity
            commonEntity.remove(engine)
        }
    }

    private fun updateEnemies(entity: Entity, deltaTime: Float) {
        val unitComponent = UnitComponent.mapper.get(entity)
        unitComponent.nearObjects = mapService.getNearObjects(unitComponent.scanRange, unitComponent.toArea)
        unitComponent.nearEnemies = findEnemies(entity)
        if (unitComponent.enableAttacking) {
            unitComponent.attackTask.update(deltaTime)
        }
    }

    private fun findEnemies(entity: Entity): List<Entity> {
        val unitComponent = UnitComponent.mapper.get(entity)
        return unitComponent.nearObjects
                .filter { UnitComponent.mapper.has(it) }
                .filter {
                    val otherUnit = UnitComponent.mapper.get(it)
                    otherUnit.playerName != unitComponent.playerName
                }
    }

    private fun updatePath(entity: Entity) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val positionComponent = PositionComponent.mapper.get(entity)
        val unitPosition = positionComponent.matrix4.getTranslation(tempPosition)
        if (mapService.withinArea(unitPosition, unitComponent.toArea)) {
            unitComponent.incrementPath()
        }
    }

    private fun updateTarget(entity: Entity) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val targetEnemy = UnitComponent.mapper.get(entity).targetEnemy
        if (unitComponent.moveByPath) {
            unitComponent.distance = UnitComponent.Companion.Distances.AT.distance
            unitComponent.target.set(unitComponent.graphPath[unitComponent.toPosition].position)
        }
        if (unitComponent.moveByTarget && targetEnemy != null) {
            val positionComponentEnemy = PositionComponent.mapper.get(targetEnemy)
            val targetPositionEnemy = positionComponentEnemy.matrix4.getTranslation(temp)
            unitComponent.distance = UnitComponent.Companion.Distances.MELEE.distance
            unitComponent.target.set(targetPositionEnemy.x, targetPositionEnemy.z)
        }
    }

    private fun updateMove(entity: Entity) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        val worldTransform = positionComponent.matrix4
        val unitPosition = worldTransform.getTranslation(tempPosition)
        val orientation = worldTransform.getRotation(tempOrientation)
        val target = tempTarget.set(unitComponent.target.x, unitPosition.y, unitComponent.target.y)
        val faceDirection = orientation.transform(tempDefaultFace.set(defaultFaceDirection))
        val direction = tempDirection.set(target).sub(unitPosition).nor()
        val angle = tempAngle.setFromCross(direction, faceDirection).angle
        val actualDistance = unitPosition.dst2(target)
        val needRotation = angle !in 0.0..10.0
        val needMove = actualDistance > unitComponent.distance
        when {
            needRotation -> {
                tempSpeed.set(if (tempAngle.y < 0) right else left).scl(unitComponent.speedAngular)
                physicComponent.body.angularVelocity = tempSpeed
                physicComponent.body.linearVelocity = zero
            }
            needMove -> {
                physicComponent.body.linearVelocity = direction.scl(unitComponent.speedLinear)
                physicComponent.body.angularVelocity = zero
            }
            else -> {
                physicComponent.body.linearVelocity = zero
                physicComponent.body.angularVelocity = zero
            }
        }
        unitComponent.isMoving = needRotation || needMove
    }

    private fun createDebugLines(entity: Entity, linesOut: MutableList<CommonEntity>) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val positionComponent = PositionComponent.mapper.get(entity)
        for (j in 0 until unitComponent.graphPath.count - 2) {
            val area1 = unitComponent.graphPath.get(j).position
            val area2 = unitComponent.graphPath.get(j + 1).position
            val unitPosition = positionComponent.matrix4.getTranslation(tempPosition)
            val lineRenderComponent = LineRenderComponent(
                    Vector3(area1.x, unitPosition.y, area1.y),
                    Vector3(area2.x, unitPosition.y, area2.y),
                    Color.GREEN,
                    true
            )
            val commonEntity = CommonEntity()
            commonEntity.add(lineRenderComponent)
            linesOut.add(commonEntity)
        }
    }
}