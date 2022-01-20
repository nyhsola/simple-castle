package castle.core.game.system

import castle.core.common.`object`.CommonEntity
import castle.core.common.event.EventContext
import castle.core.common.event.EventQueue
import castle.core.game.builder.PlayerBuilder
import castle.core.game.component.PlayerComponent
import castle.core.game.component.SideComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.signals.Signal
import com.badlogic.ashley.systems.IteratingSystem

class PlayerSystem(
    private val eventQueue: EventQueue,
    private val playerBuilder: PlayerBuilder
) : IteratingSystem(family), EntityListener {
    companion object {
        const val SPAWN: String = "SPAWN"
        const val PLAYER_NAME: String = "PLAYER_NAME"
        private val family: Family = Family.all(PlayerComponent::class.java).get()
    }

    private val signal = Signal<EventContext>()

    init {
        signal.add(eventQueue)
    }

    @Suppress("UNCHECKED_CAST")
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val playerComponent = PlayerComponent.mapper.get(entity)
        val playerName = playerComponent.playerJson.playerName
        eventQueue.proceed { eventContext ->
            when (eventContext.eventType) {
                SPAWN -> {
                    val playerNameEvent = eventContext.params[PLAYER_NAME]
                    val isPlayers = playerName == playerNameEvent
                    if (isPlayers) {
                        playerBuilder.buildUnits(playerComponent.playerJson)
                            .onEach { it.add(engine) }
                    }
                    isPlayers
                }
                else -> false
            }
        }
    }

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val playerComponent = PlayerComponent.mapper.get(entity)
        val playerName = playerComponent.playerJson.playerName
        playerBuilder.buildBuildings(playerComponent.playerJson)
            .onEach { it.add(SideComponent(playerName)) as CommonEntity }
            .onEach { it.add(engine) }
//        val map = engine.entities
//            .filter { RenderComponent.mapper.has(it) }
//            .associateBy { RenderComponent.mapper.get(it).nodeName }
//        for (list in playerComponent.playerJson.pathSettings.paths) {
//            val startPoint = list[0]
//            val positionComponent = PositionComponent.mapper.get(map[startPoint])
//            val commonEntity = CommonEntity()
//            commonEntity.add(TextComponent("", positionComponent.matrix4.getTranslation(Vector3()).scl(-1f, 1f, 1f)))
//            commonEntity.add(TextCountComponent(999, playerName, SPAWN, signal))
//            commonEntity.add(engine)
//        }
    }

    override fun entityRemoved(entity: Entity) {
    }
}