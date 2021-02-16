package castle.server.ashley.game

import castle.server.ashley.game.unit.MovableUnit
import castle.server.ashley.service.GameContext
import castle.server.ashley.utils.json.PlayerJson
import com.badlogic.gdx.utils.Disposable

class Player(playerJson: PlayerJson, private val gameContext: GameContext) : Disposable {
    private val paths: List<List<String>> = playerJson.paths
    private val baseUnits: MutableList<MovableUnit> = ArrayList()
    private val unitType: String = playerJson.unitType
    private val spawnRate: Float = playerJson.spawnRate
    private var accumulate: Float = 0.0f

    fun update(delta: Float) {
        accumulate += delta
        while (accumulate >= spawnRate) {
            accumulate -= spawnRate
            paths.forEach { createUnit(it) }
        }
        baseUnits.forEach { it.update(delta) }
    }

    private fun createUnit(path: List<String>) {
        val paths = path.map { gameContext.resourceManager.constructorMap[it]!!.getMatrix4() }
        val movableUnit = MovableUnit(gameContext.resourceManager.constructorMap[unitType]!!, paths, gameContext)
        gameContext.engine.addEntity(movableUnit.entity)
        baseUnits.add(movableUnit)
    }

    override fun dispose() {
        baseUnits.forEach { it.dispose() }
    }
}