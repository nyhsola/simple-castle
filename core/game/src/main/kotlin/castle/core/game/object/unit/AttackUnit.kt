package castle.core.game.`object`.unit

import castle.core.game.GameContext
import castle.core.game.`object`.DebugLine
import castle.core.game.`object`.GameMap
import castle.core.game.utils.Constructor
import castle.core.physic.service.PhysicService
import com.badlogic.gdx.math.Matrix4

class AttackUnit(
    paths: List<Pair<String, Matrix4>>,
    constructor: Constructor,
    gameContext: GameContext,
    private val gameMap: GameMap,
    physicService: PhysicService
) : MovableUnit(paths, constructor, gameContext, gameMap, physicService) {
    private val debugLine: DebugLine = DebugLine(gameContext)

    override fun update() {
//        val enemiesNear = getEnemiesNear()
//        if (enemiesNear.isNotEmpty()) {
//            val enemyPosition = enemiesNear[0].unitPosition
//            debugLine.show = false
//            debugLine.from = unitPosition
//            debugLine.to = enemyPosition
//            debugLine.color = Color.RED
////            stand()
//        }
        super.update()
    }

    override fun dispose() {
        debugLine.dispose()
        super.dispose()
    }

    private fun getEnemiesNear(): List<GameObject> {
        return gameMap.getNearObjects(unitPosition)
    }
}