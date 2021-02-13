package castle.server.ashley.game

import castle.server.ashley.component.RectComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Minimap(engine: Engine, gameMap: GameMap) {
    companion object {
        const val miniMapWidth = 300f
        const val miniMapHeight = 300f

        const val startPositionX = 1615f
        const val startPositionY = 5f
    }

    private val pieces: MutableList<MutableList<MinimapPiece>>

    init {
        pieces = ArrayList()

        val miniMap = gameMap.flatMap.map { it.toMutableList() }.toMutableList()

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

        for (i in miniMap.indices) {
            pieces.add(ArrayList())
            for (j in miniMap[i].indices) {
                val area = MinimapPiece(engine)
                area.setWidth(pointWidth)
                area.setHeight(pointHeight)
                area.setPosition(Vector2(i.toFloat(), j.toFloat()), Vector3(startPositionX, startPositionY, 0f))
                area.setValue(miniMap[i][j])
                pieces[i].add(area)
            }
        }
    }

    private class MinimapPiece(engine: Engine) {
        private val entity = engine.createEntity()
        private val rectComponent: RectComponent = engine.createComponent(RectComponent::class.java)

        init {
            rectComponent.position = Vector2()
            engine.addEntity(entity.apply {
                add(rectComponent)
            })
        }

        fun setPosition(paramIndex: Vector2, offsetParam: Vector3) {
            rectComponent.position.set(offsetParam.x + paramIndex.x * rectComponent.width, offsetParam.y + paramIndex.y * rectComponent.height)
        }

        fun setWidth(width: Float) {
            rectComponent.width = width
        }

        fun setHeight(height: Float) {
            rectComponent.height = height
        }

        fun setValue(value: Int) {
            val baseHsv = FloatArray(3)
            Color.valueOf("#00ff4c").toHsv(baseHsv)
            baseHsv[2] = baseHsv[2] - 0.2f * value
            val color: Color = Color(0f, 0f, 0f, 0f).fromHsv(baseHsv)
            rectComponent.color = color
        }
    }
}