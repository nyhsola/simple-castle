package castle.core.service.game

import castle.core.builder.EffectBuilder
import castle.core.builder.PlayerBuilder
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.json.PlayerJson
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal

class Player(
    private val playerJson: PlayerJson,
    private val playerBuilder: PlayerBuilder,
    private val effectBuilder: EffectBuilder
) {
    private val buildings: MutableList<Entity> = ArrayList()
    private val effects: MutableList<CountText> = ArrayList()

    private val internalPlayerEvents: EventQueue = EventQueue()
    private val signal: Signal<EventContext> = Signal()

    init {
        signal.add(internalPlayerEvents)
    }

    fun init(engine: Engine) {
        val buildingsCreated = playerBuilder.buildBuildings(playerJson).onEach { engine.addEntity(it) }
        buildings.addAll(buildingsCreated)

        val effectsCreated = effectBuilder.buildCountText(playerJson, signal).onEach { engine.addEntity(it) }
        effects.addAll(effectsCreated)
    }

    fun update(engine: Engine, deltaTime: Float) {
        effects.onEach { it.update(deltaTime) }
        internalEvents(engine)
    }

    fun spawnUnits(engine: Engine) {
        List(playerJson.paths.size) { index -> playerBuilder.buildUnit(playerJson, index)}.onEach { engine.addEntity(it) }
    }

    private fun internalEvents(engine: Engine) {
        internalPlayerEvents.proceed { eventContext ->
            when (eventContext.eventType) {
                CountText.ON_COUNT -> {
                    val lineNumber = eventContext.params.getValue(CountText.PARAM_LINE) as Int
                    engine.addEntity(playerBuilder.buildUnit(playerJson, lineNumber))
                    true
                }
                else -> false
            }
        }
    }
}