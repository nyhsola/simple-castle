package castle.core.game.system

import castle.core.common.component.PositionComponent
import castle.core.common.system.IteratingIntervalSystem
import castle.core.game.component.PathComponent
import castle.core.game.service.MapService
import castle.core.game.service.NeutralInitService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Vector3
import java.lang.Integer.min

class PathSystem(
    private val neutralInitService: NeutralInitService,
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
        val pathComponent = PathComponent.mapper.get(entity)
        val pathPositions = pathComponent.path
            .map { neutralInitService.neutralUnits[it] }
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
        val currentPosition = pathComponent.nextPosition
        val currentArea = graph[currentPosition]
        val unitPosition = worldTransform.getTranslation(tempPosition)
        val nextPosition = currentPosition + 1
        if (nextPosition < graph.count && mapService.withinArea(unitPosition, currentArea)) {
            pathComponent.nextPosition = min(nextPosition, graph.count - 1)
        }
    }
}