package castle.core.`object`

import castle.core.builder.TextBuilder
import castle.core.builder.UnitBuilder
import castle.core.component.UnitComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.json.PlayerJson
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Disposable

class Player(
    private val eventQueue: EventQueue,
    private val engine: Engine,
    private val playerJson: PlayerJson,
    private val unitBuilder: UnitBuilder,
    textBuilder: TextBuilder
) : Disposable {
    private val buildings: List<Entity> = playerJson.units.map { spawnBuilding(playerJson.playerName, it.key, it.value) }
    private val effects: List<CountText> = textBuilder.build(playerJson, eventQueue)
    private val units: MutableList<Entity> = ArrayList()

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(CountText.ON_COUNT) {
            spawnUnit(it.params.getValue(CountText.PARAM_LINE) as Int)
        }
    )

    fun init() {
        buildings.onEach { engine.addEntity(it) }
        effects.onEach { engine.addEntity(it) }
    }

    fun update(deltaTime: Float) {
        effects.onEach { it.update(deltaTime) }
        eventQueue.proceed(operations)
    }

    fun spawnUnits() {
        List(playerJson.paths.size) { index -> spawnUnit(index) }
    }

    private fun spawnBuilding(playerName: String, unitStr: String, spawnStr: String): Entity {
        val unit = unitBuilder.build(unitStr, spawnStr)
        UnitComponent.mapper.get(unit).playerName = playerName
        return unit
    }

    private fun spawnUnit(lineNumber: Int): Entity {
        val path = playerJson.paths[lineNumber]
        val spawn = path[0]
        val entity = unitBuilder.build("warrior", spawn)
        val unitComponent = UnitComponent.mapper.get(entity)
        unitComponent.path.addAll(path)
        unitComponent.playerName = playerJson.playerName
        engine.addEntity(entity)
        units.add(entity)
        return entity
    }

    override fun dispose() {
        buildings.onEach { engine.removeEntity(it) }
        units.onEach { engine.removeEntity(it) }
        effects.onEach { engine.removeEntity(it) }
    }
}