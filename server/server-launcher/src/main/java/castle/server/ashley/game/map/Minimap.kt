package castle.server.ashley.game.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Minimap(engine: Engine, gameMap: GameMap) {
    private companion object {
        private val miniMapWidth: Float = Gdx.graphics.width * 0.2f
        private val miniMapHeight: Float = Gdx.graphics.height * 0.3f
        private val startPositionX = Gdx.graphics.width - miniMapWidth - 10f
        private const val startPositionY = 10f
    }

    private val pieces: MutableList<MutableList<MinimapPiece>>
    private val buffer: MutableList<Pair<Int, Int>> = ArrayList()

    init {
        val miniMap = gameMap.map2D.map { it.toMutableList() }.toMutableList()

        for (i in miniMap.indices) {
            for (j in miniMap[i].indices) {
                if (i > j) {
                    val temp = miniMap[i][j]
                    miniMap[i][j] = miniMap[j][i]
                    miniMap[j][i] = temp
                }
            }
        }

        val pointWidth = miniMapWidth / miniMap.size
        val pointHeight = miniMapHeight / miniMap[0].size

        pieces = ArrayList()
        for (i in miniMap.indices) {
            pieces.add(ArrayList())
            for (j in miniMap[i].indices) {
                pieces[i].add(
                    MinimapPiece(
                        engine,
                        pointWidth, pointHeight,
                        miniMap[i][j],
                        Vector2(i.toFloat(), j.toFloat()), Vector3(startPositionX, startPositionY, 0f)
                    )
                )
            }
        }
    }

    fun reset() {
        buffer.forEach {
            pieces[it.first][it.second].reset()
        }
        buffer.clear()
    }

    fun set(i: Int, j: Int) {
        if (i < 0 || j < 0) {
            return
        }

        buffer.add(Pair(j, i))
        pieces[j][i].setColor(Color.RED)
    }
}