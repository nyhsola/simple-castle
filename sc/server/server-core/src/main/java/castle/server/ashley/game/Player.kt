package castle.server.ashley.game

import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.json.PlayerJson
import com.badlogic.ashley.core.Engine

class Player(playerJson: PlayerJson, private val constructorMap: Map<String, Constructor>) {
    private val paths: List<List<String>> = playerJson.paths
    private val baseUnits: MutableList<MovingUnit> = ArrayList()
    private val unitType: String = playerJson.unitType
    private val spawnRate: Float = playerJson.spawnRate
    private var accumulate: Float = 0.0f

    fun update(engine: Engine, delta: Float) {
        accumulate += delta
        while (accumulate >= spawnRate) {
            accumulate -= spawnRate
            paths.forEach { createUnit(engine, it) }
        }
        baseUnits.forEach { it.update(delta) }
    }

    private fun createUnit(engine: Engine, path: List<String>) {
        val paths = path.map { constructorMap[it]!!.getMatrix4() }
        val baseUnit = MovingUnit(engine, constructorMap[unitType]!!, paths)
        baseUnits.add(baseUnit)
    }
}