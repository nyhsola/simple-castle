package castle.server.ashley.app

import castle.core.common.creator.GUIConfig
import castle.server.ashley.screen.GameScreen
import com.badlogic.gdx.Screen
import ktx.app.KtxGame

class ServerGame(private val guiConfig: GUIConfig) : KtxGame<Screen>() {
    override fun create() {
        addScreen(GameScreen(guiConfig))
        setScreen<GameScreen>()
    }
}