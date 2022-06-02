package castle.core.config

import castle.core.service.CommonResources
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable

class CommonConfig : Disposable {
    val spriteBatch = SpriteBatch()
    val commonResources = CommonResources()

    override fun dispose() {
        spriteBatch.dispose()
    }
}