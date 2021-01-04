package castle.server.ashley.service

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PlayerComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.UnitComponent
import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3

class PlayerService(private val resourceManager: ResourceManager) {
    companion object {
        const val speedScalar: Float = 1f
        val angularDefaultFactor: Vector3 = Vector3(0.0f, 1.0f, 0.0f)
        val anglePrecision = 0.0..10.0
        val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
        val right: Vector3 = Vector3(0f, 1f, 0f)
        val left: Vector3 = Vector3(0f, -1f, 0f)
        val noMove: Vector3 = Vector3(0f, 0f, 0f)
    }

    private val constructorMap: Map<String, Constructor> = resourceManager.sceneObjectsJson
        .map { sceneObjectJson -> Constructor(resourceManager.model, sceneObjectJson) }
        .associateBy(keySelector = { constructor -> constructor.node })
        .toMap()

    fun createGameEnvironment(engine: Engine) {
        constructorMap
            .asIterable()
            .filter { entry -> entry.value.instantiate }
            .forEach { entry -> engine.addEntity(entry.value.instantiate(engine)) }
//        resourceManager.players.forEach {
//            val entity = engine.createEntity()
//            val playerComponent = PlayerComponent.createComponent(engine, it)
//            entity.add(playerComponent)
//            engine.addEntity(entity)
//        }
    }

    private fun createUnit(engine: Engine, entity: Entity, spawn: Constructor, aim: Constructor) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        positionComponent.setPositionFrom(spawn.getTransform())
        physicComponent.setAngularFactor(angularDefaultFactor)
        val createComponent = UnitComponent.createComponent(engine, aim)
        entity.add(createComponent)
        engine.addEntity(entity)
    }

    private fun createUnits(engine: Engine, playerComponent: PlayerComponent) {
        playerComponent.paths.forEach { path ->
            val unit = constructorMap.getValue(playerComponent.unitType).instantiate(engine)
            val spawn = constructorMap.getValue(path.component1())
            val area = constructorMap.getValue(path.component2())
            createUnit(engine, unit, spawn, area)
        }
    }

//    override fun processEntity(entity: Entity?, deltaTime: Float) {
//        val playerComponent = PlayerComponent.mapper.get(entity)
//        playerComponent.accumulate += deltaTime
//        while (playerComponent.accumulate >= playerComponent.spawnRate) {
//            playerComponent.accumulate -= playerComponent.spawnRate
//            createUnits(playerComponent)
//        }
//    }
//    override fun processEntity(entity: Entity?, deltaTime: Float) {
//        val unitComponent = UnitComponent.mapper.get(entity)
//        val positionComponent = PositionComponent.mapper.get(entity)
//        val physicComponent = PhysicComponent.mapper.get(entity)
//
//        moveProceed(positionComponent, unitComponent, physicComponent)
//    }
//
//    private fun moveProceed(positionComponent: PositionComponent, unitComponent: UnitComponent, physicComponent: PhysicComponent) {
//        val unitPosition = positionComponent.matrix4.getTranslation(Vector3())
//        val targetDirection = unitComponent.aim.cpy().sub(unitPosition).nor().scl(speedScalar)
//
//        val faceDirection = physicComponent.physicObject.body.orientation.transform(defaultFaceDirection.cpy())
//        val diffAngle = getAngle(targetDirection, faceDirection)
//
//        val previousAngle = unitComponent.previousAngle
//        val rotateDirection = unitComponent.rotateDirection
//
//        val angularVelocity: Vector3
//
//        if (diffAngle !in anglePrecision) {
//            if (diffAngle - previousAngle > 0) {
//                unitComponent.rotateDirection = !rotateDirection
//            }
//            angularVelocity = if (unitComponent.rotateDirection) right.cpy() else left.cpy()
//            unitComponent.previousAngle = diffAngle
//        } else {
//            angularVelocity = noMove.cpy()
//        }
//
//        physicComponent.physicObject.body.angularVelocity = angularVelocity
//        physicComponent.physicObject.body.linearVelocity = faceDirection
//    }
//
//    private fun getAngle(a: Vector3, b: Vector3): Double = Math.toDegrees(acos(a.dot(b).toDouble()))
}