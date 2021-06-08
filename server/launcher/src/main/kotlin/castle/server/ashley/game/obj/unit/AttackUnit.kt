package castle.server.ashley.game.obj.unit

import castle.server.ashley.game.GameContext
import castle.server.ashley.game.obj.DebugLine
import castle.server.ashley.utils.Constructor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class AttackUnit(
    paths: List<Matrix4>,
    constructor: Constructor,
    private val gameContext: GameContext
) : MovableUnit(constructor, gameContext) {
    private val tempVector1: Vector3 = Vector3()
    private val debugLine: DebugLine = DebugLine(gameContext)

    init {
        unitPosition = paths[0].getTranslation(tempVector1)
        walkTo(paths[1].getTranslation(tempVector1))
    }

    override fun update() {
        val enemiesNear = getEnemiesNear()
        if (enemiesNear.isNotEmpty()) {
            val enemyPosition = enemiesNear[0].unitPosition
            debugLine.show = false
            debugLine.from = unitPosition
            debugLine.to = enemyPosition
            debugLine.color = Color.RED
        }
        super.update()
    }

    override fun dispose() {
        debugLine.dispose()
        super.dispose()
    }

    private fun getEnemiesNear(): List<GameObject> {
        return gameContext.gameMap.getNearObjects(unitPosition)
    }
}