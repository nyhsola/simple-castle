package castle.core.game.system

import castle.core.game.service.UIService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import ktx.app.KtxInputAdapter

class UISystem(
    private var uiService: UIService
) : IntervalSystem(GAME_TICK), KtxInputAdapter {
    private companion object {
        const val GAME_TICK: Float = 0.1f
    }

    override fun addedToEngine(engine: Engine) {
        uiService.add(engine)
    }

    override fun updateInterval() {
        uiService.update()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return uiService.touchDown(screenX, screenY, pointer, button)
    }
}