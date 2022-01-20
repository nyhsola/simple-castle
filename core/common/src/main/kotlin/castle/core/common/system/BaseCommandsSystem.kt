package castle.core.common.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.app.KtxInputAdapter

class BaseCommandsSystem : EntitySystem(), KtxInputAdapter {
    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.ESCAPE -> {
                Gdx.app.exit()
                true
            }
            else -> super.keyDown(keycode)
        }
    }
}