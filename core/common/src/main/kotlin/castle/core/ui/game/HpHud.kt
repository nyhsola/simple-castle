package castle.core.ui.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table

class HpHud : Table() {
    class SomeActor(private val texture: Texture) : Actor() {
        override fun draw(batch: Batch, alpha: Float) {
            batch.draw(texture, x, y, width, height)
        }
    }

    fun addHp(someActor: SomeActor) {
        addActor(someActor)
    }

    fun removeHp(someActor: SomeActor) {
        removeActor(someActor)
    }
}