package castle.core.ui.game

import castle.core.path.Area
import castle.core.service.MapScanService
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Widget

class Minimap(
    private val shapeRenderer: ShapeRenderer, private val mapScanService: MapScanService
) : Widget() {
    private val minimap: MutableList<MutableList<MinimapPiece>> = ArrayList()
    private val minimapBuffer: MutableList<MinimapPiece> = ArrayList()

    init {
//        addListener(object : DragListener() {
//            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
//                moveBy(x - width / 2, y - height / 2)
//            }
//        })
    }

    fun init() {
        initializeMinimap(mapScanService.map)
    }

    override fun sizeChanged() {
        val pointWidth = width / minimap.size
        val pointHeight = height / minimap[0].size
        minimap.forEach { miniMapRow ->
            miniMapRow.forEach { miniMapCol ->
                miniMapCol.width = pointWidth
                miniMapCol.height = pointHeight
                miniMapCol.offset = Vector2(x, y)
            }
        }
        super.sizeChanged()
    }

    override fun getPrefWidth(): Float {
        return 100f
    }

    override fun getPrefHeight(): Float {
        return 100f
    }

    fun update(objectsOnMap: Map<Area, Collection<Entity>>) {
        minimapBuffer.forEach { it.reset() }
        minimapBuffer.clear()
        objectsOnMap.filter { it.value.isNotEmpty() }.forEach {
            val area = it.key
            minimapBuffer.add(minimap[area.y][area.x])
            minimap[area.y][area.x].color = Color.RED
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.end()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.transformMatrix = batch.transformMatrix
        shapeRenderer.begin()
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled)
        for (minimapPiece in minimap.flatten()) {
            shapeRenderer.color = minimapPiece.color
            shapeRenderer.rect(
                minimapPiece.position.x + x, minimapPiece.position.y + y, minimapPiece.width, minimapPiece.height
            )
        }
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
        batch.begin()
    }

    private fun initializeMinimap(minimapParam: List<List<Int>>) {
        for (i in minimapParam.indices) {
            minimap.add(ArrayList())
            for (j in minimapParam[i].indices) {
                minimap[i].add(MinimapPiece(Vector2(i.toFloat(), j.toFloat()), minimapParam[i][j]))
            }
        }
    }

    private class MinimapPiece(
        private val index: Vector2, private val groundHeight: Int
    ) {
        var width: Float = 0f
        var height: Float = 0f
        var color: Color = Color.WHITE
        var offset = Vector2()
            set(value) {
                field = value
                updatePosition()
            }
        var position: Vector2 = Vector2()
            set(value) {
                field = value
                updatePosition()
            }

        init {
            reset()
        }

        fun reset() {
            color = getGroundColor(groundHeight)
        }

        private fun updatePosition() {
            position.set(
                offset.x + index.x * width, offset.y + index.y * height
            )
        }

        private fun getGroundColor(value: Int): Color {
            val baseHsv = FloatArray(3)
            Color.valueOf("#00ff4c").toHsv(baseHsv)
            baseHsv[2] = baseHsv[2] - 0.2f * value
            val hsv = Color().fromHsv(baseHsv)
            hsv.a = 0.7f
            return hsv
        }
    }
}