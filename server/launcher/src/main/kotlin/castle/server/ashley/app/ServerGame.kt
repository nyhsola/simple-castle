package castle.server.ashley.app

import castle.core.screen.GameScreen
import com.badlogic.gdx.Screen
import ktx.app.KtxGame

class ServerGame : KtxGame<Screen>() {
    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}