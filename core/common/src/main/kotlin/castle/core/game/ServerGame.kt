package castle.core.game

import castle.core.event.EventQueue
import castle.core.screen.GameScreen
import castle.core.screen.StartScreen
import com.badlogic.gdx.Screen
import ktx.app.KtxGame

class ServerGame : KtxGame<Screen>() {
    private val eventQueue = EventQueue()

    override fun create() {
        addScreen(StartScreen(eventQueue))
        addScreen(GameScreen())
        setScreen<StartScreen>()
    }

    override fun render() {
        proceedEvent()
        super.render()
    }

    private fun proceedEvent() {
        eventQueue.proceed {
            when (it.eventType) {
                StartScreen.GAME_EVENT -> {
                    setScreen<GameScreen>()
                }
            }
            false
        }
    }
}