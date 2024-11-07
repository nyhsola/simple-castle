package castle.core.ui.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table

class HpHud(
    private val shapeRenderer: ShapeRenderer
) : Table() {
    inner class HpBarRender(rectWidth: Float) : Actor() {
        private var borderThickness: Float = 2f
        var percent: Float = 1f

        init {
            setPosition(0f, 0f)
            setSize(rectWidth, 7f)
        }

        override fun draw(batch: Batch, parentAlpha: Float) {
            batch.end()
            shapeRenderer.projectionMatrix = stage.camera.combined
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

            shapeRenderer.color = Color.BLACK
            shapeRenderer.rect(x, y, width, height)

            shapeRenderer.color = Color.GREEN
            shapeRenderer.rect(
                x + borderThickness,
                y + borderThickness,
                width * percent - 2 * borderThickness,
                height - 2 * borderThickness)

            shapeRenderer.end()
            batch.begin()
        }
    }

    class HpBar(val width: Float) {
        lateinit var hpBarRender: HpBarRender

        fun setCameraProject(projected: Vector3) {
            hpBarRender.setPosition(projected.x - width / 2, projected.y)
        }

        fun setPercent(percent: Float) {
            hpBarRender.percent = percent
        }
    }

    fun addHp(hpBar: HpBar) {
        hpBar.hpBarRender = HpBarRender(hpBar.width)
        addActor(hpBar.hpBarRender)
    }

    fun removeHp(hpBar: HpBar) {
        removeActor(hpBar.hpBarRender)
    }
}