package castle.core.game.`object`

import castle.core.game.GameContext
import castle.core.game.`object`.unit.AttackUnit
import castle.core.game.`object`.unit.GameObject
import castle.core.game.`object`.unit.MovableUnit
import castle.core.common.json.PlayerJson
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class Player(
    playerJson: PlayerJson,
    private val gameContext: GameContext,
    private val gameMap: GameMap
) : Disposable {
    private val paths: List<List<String>> = playerJson.paths
    private val baseUnits: MutableList<MovableUnit> = ArrayList()
    private val unitType: String = playerJson.unitType
    private val spawnRate: Float = playerJson.spawnRate
    private var accumulate: Float = 0.0f

    fun update(delta: Float) {
        accumulate += delta
        while (accumulate >= spawnRate) {
            accumulate -= spawnRate
            spawn()
        }
        baseUnits.forEach { it.update() }
    }

    fun spawn() {
        paths.forEach { createUnit(it) }
    }

    fun getUnits(): List<GameObject> {
        return baseUnits
    }

    private fun createUnit(path: List<String>) {
        val paths = path.map {
            val constructor = gameContext.resourceService.constructorMap[it]!!
            constructor.getMatrix4().getTranslation(Vector3())
        }

        val constructor = gameContext.resourceService.constructorMap[unitType]!!
        val unit = AttackUnit(constructor, gameContext, gameMap)

        if (paths.isNotEmpty()) {
            unit.unitPosition = paths[0]
            unit.startRoute(paths)
            unit.lookAt(unit.nextPoint)
        }

        baseUnits.add(unit)
    }

    override fun dispose() {
        baseUnits.forEach { it.dispose() }
    }
}