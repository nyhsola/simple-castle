package castle.core.game.system

import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.common.system.IteratingIntervalSystem
import castle.core.game.component.PathComponent
import castle.core.game.service.MapService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Vector3
import java.lang.Integer.min

class PathSystem(
    private val mapService: MapService
) : IteratingIntervalSystem(PATH_TICK, family), EntityListener {
    private companion object {
        private val family: Family = Family.all(PositionComponent::class.java, PathComponent::class.java).get()
        private const val PATH_TICK: Float = 1f / 10f
    }

    private val tempPosition: Vector3 = Vector3()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val map = engine.entities
            .filter { PositionComponent.mapper.has(entity) && RenderComponent.mapper.has(entity) }
            .associateBy { RenderComponent.mapper.get(entity).nodeName }
        val pathComponent = PathComponent.mapper.get(entity)
        val pathPositions = pathComponent.path
            .map { map[it] }
            .map { PositionComponent.mapper.get(it).matrix4.getTranslation(tempPosition).cpy() }
        pathComponent.graphPath = mapService.getPath(pathPositions)
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val pathComponent = PathComponent.mapper.get(entity)
        val worldTransform = positionComponent.matrix4
        val graph = pathComponent.graphPath
        val currentPosition = pathComponent.currentPosition
        val currentArea = graph[currentPosition]
        val unitPosition = worldTransform.getTranslation(tempPosition)
        val nextPosition = currentPosition + 1
        if (nextPosition < graph.count && mapService.withinArea(unitPosition, currentArea)) {
            pathComponent.currentPosition = min(nextPosition, graph.count - 1)
        }
    }
}