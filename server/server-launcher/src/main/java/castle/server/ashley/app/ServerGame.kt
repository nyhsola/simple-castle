package castle.server.ashley.app

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.app.screen.GameScreen
import com.badlogic.gdx.Screen
import ktx.app.KtxGame

class ServerGame(private val guiCreator: GUICreator) : KtxGame<Screen>() {
    override fun create() {
        addScreen(GameScreen(guiCreator))
        setScreen<GameScreen>()
    }
}