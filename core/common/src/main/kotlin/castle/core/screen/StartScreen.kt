package castle.core.screen

import castle.core.event.EventContext
import castle.core.event.EventQueue
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

class StartScreen(eventQueue: EventQueue) : KtxScreen, KtxInputAdapter {
    companion object {
        const val GAME_EVENT = "GAME_EVENT"
    }

    private val signal = Signal<EventContext>()
    private val inputMultiplexer = InputMultiplexer()

    init {
        signal.add(eventQueue)
        inputMultiplexer.addProcessor(this)
    }

    override fun show() {
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        signal.dispatch(EventContext(GAME_EVENT))
        return super.touchDown(screenX, screenY, pointer, button)
    }
}