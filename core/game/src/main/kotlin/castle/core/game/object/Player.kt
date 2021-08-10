package castle.core.game.`object`

import castle.core.game.GameContext
import castle.core.game.`object`.unit.AttackUnit
import castle.core.game.`object`.unit.GameObject
import castle.core.game.`object`.unit.MovableUnit
import castle.core.game.utils.json.PlayerJson
import castle.core.physic.service.PhysicService
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class Player(
    playerJson: PlayerJson,
    private val gameContext: GameContext,
    private val gameMap: GameMap,
    private val physicService: PhysicService
) : Disposable {
    private val paths: List<List<String>> = playerJson.paths
    private val baseUnits: MutableList<MovableUnit> = ArrayList()
    private val unitType: String = playerJson.unitType
    private val spawnRate: Float = playerJson.spawnRate
    private var accumulate: Float = 0.0f
    private val tempVector = Vector3()

    fun update(delta: Float) {
        accumulate += delta
        while (accumulate >= spawnRate) {
            accumulate -= spawnRate
            paths.forEach { createUnit(it) }
        }
        baseUnits.forEach { it.update() }
    }

    fun getUnits(): List<GameObject> {
        return baseUnits
    }

    private fun createUnit(path: List<String>) {
        val paths = path.map {
            val constructor = gameContext.resourceManager.constructorMap[it]!!
            Pair(constructor.node, constructor.getMatrix4())
        }

        val constructor = gameContext.resourceManager.constructorMap[unitType]!!
        val unit = AttackUnit(constructor, gameContext, gameMap, physicService)

        if (paths.isNotEmpty()) {
            unit.unitPosition = paths[0].second.getTranslation(tempVector)
            unit.initWalking(paths)
        }

        baseUnits.add(unit)
    }

    override fun dispose() {
        baseUnits.forEach { it.dispose() }
    }
}