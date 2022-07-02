package castle.core.system

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.component.render.LineRenderComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.physic.PhysicListener
import castle.core.service.MapService
import castle.core.service.PhysicService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class UnitSystem(
    private val eventQueue: EventQueue,
    private val mapService: MapService,
    private val physicService: PhysicService
) : IteratingSystem(family), PhysicListener {
    companion object {
        const val DEBUG_ENABLE = "DEBUG_PATH_ENABLE"
        private val family: Family = Family.all(UnitComponent::class.java, PositionComponent::class.java, PhysicComponent::class.java).get()
    }

    private var debugEnabled: Boolean = false
    private val temp: Vector3 = Vector3()
    private val lines: MutableList<Entity> = ArrayList()

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(DEBUG_ENABLE) {
            debugEnabled = !debugEnabled
            if (debugEnabled) {
                engine.getEntitiesFor(family).onEach { createDebugLines(it, lines) }
                lines.onEach { engine.addEntity(it) }
            } else {
                lines.onEach { engine.removeEntity(it) }
                lines.clear()
            }
        }
    )

    override fun addedToEngine(engine: Engine) {
        physicService.addListener(this)
        super.addedToEngine(engine)
    }

    override fun update(deltaTime: Float) {
        eventQueue.proceed(operations)
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val positionComponent = PositionComponent.mapper.get(entity)
        unitComponent.currentArea = mapService.toArea(positionComponent.matrix4.getTranslation(temp))
        if (unitComponent.deleteMe) {
            engine.removeEntity(entity)
        }
        if (unitComponent.isDead) {
            unitComponent.deleteMe = true
        }
    }

    private fun createDebugLines(entity: Entity, linesOut: MutableList<Entity>) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val positionComponent = PositionComponent.mapper.get(entity)
        for (j in 0 until unitComponent.mainPath.count - 2) {
            val area1 = unitComponent.mainPath.get(j).position
            val area2 = unitComponent.mainPath.get(j + 1).position
            val unitPosition = positionComponent.matrix4.getTranslation(temp)
            val lineRenderComponent = LineRenderComponent(
                Vector3(area1.x, unitPosition.y, area1.y), Vector3(area2.x, unitPosition.y, area2.y), Color.GREEN, true
            )
            val lineEntity = Entity()
            lineEntity.add(lineRenderComponent)
            linesOut.add(lineEntity)
        }
    }

    override fun onContactStarted(entity1: Entity, entity2: Entity) {
        if (!UnitComponent.mapper.has(entity1) || !UnitComponent.mapper.has(entity2)) return
        val unitComponent1 = UnitComponent.mapper.get(entity1)
        val unitComponent2 = UnitComponent.mapper.get(entity2)
        unitComponent1.inTouchObjects.add(entity2)
        unitComponent2.inTouchObjects.add(entity1)
    }

    override fun onContactEnded(entity1: Entity, entity2: Entity) {
        if (!UnitComponent.mapper.has(entity1) || !UnitComponent.mapper.has(entity2)) return
        val unitComponent1 = UnitComponent.mapper.get(entity1)
        val unitComponent2 = UnitComponent.mapper.get(entity2)
        unitComponent1.inTouchObjects.remove(entity2)
        unitComponent2.inTouchObjects.remove(entity1)
    }
}