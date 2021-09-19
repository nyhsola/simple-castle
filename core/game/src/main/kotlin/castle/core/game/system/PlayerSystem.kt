package castle.core.game.system

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.game.component.PlayerComponent
import castle.core.game.component.SideComponent
import castle.core.game.component.TextComponent
import castle.core.game.service.GameResourceService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector3

class PlayerSystem(private val gameResourceService: GameResourceService) : IteratingSystem(family), EntityListener {
    private companion object {
        private val family: Family = Family.all(PlayerComponent::class.java).get()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
    }

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val playerComponent = PlayerComponent.mapper.get(entity)
        val units = playerComponent.playerJson
            .buildStartup(gameResourceService)
            .map { it.add(SideComponent(playerComponent.playerJson.playerName)) as CommonEntity }
            .onEach { it.add(engine) }
        playerComponent.units.addAll(units)
        val map = engine.entities
            .filter { RenderComponent.mapper.has(it) }
            .associateBy { RenderComponent.mapper.get(it).nodeName }
        for (list in playerComponent.playerJson.pathSettings.paths) {
            val startPoint = list[0]
            val positionComponent = PositionComponent.mapper.get(map[startPoint])
            val commonEntity = CommonEntity()
            commonEntity.add(TextComponent("30", positionComponent.matrix4.getTranslation(Vector3())))
            commonEntity.add(engine)
        }
    }

    override fun entityRemoved(entity: Entity) {
    }
}