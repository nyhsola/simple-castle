package castle.core.config

import castle.core.event.EventQueue
import castle.core.service.CommonResources
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable

class CommonConfig : Disposable {
    val eventQueue = EventQueue()
    val spriteBatch = SpriteBatch()
    val commonResources = CommonResources()

    override fun dispose() {
        spriteBatch.dispose()
        commonResources.dispose()
    }
}