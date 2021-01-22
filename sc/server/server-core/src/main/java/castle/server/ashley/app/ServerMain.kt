package castle.server.ashley.app

import castle.server.ashley.creator.GUICreator
import castle.server.ashley.screen.GameScreen
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.bullet.Bullet

class ServerMain(guiCreator: GUICreator) : Game() {
    private val gameScreen: GameScreen by lazy { GameScreen(guiCreator) }

    override fun create() {
        Bullet.init(false, false)
        setScreen(gameScreen)
        Gdx.input.inputProcessor = gameScreen
    }

    override fun dispose() {
        super.dispose()
        gameScreen.dispose()
    }

}