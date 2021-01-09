package castle.server.ashley.screen

import castle.server.ashley.creator.GUICreator
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.bullet.Bullet

class ServerGame(guiCreator: GUICreator) : Game() {
    private val gameScreen: GameScreen by lazy { GameScreen(guiCreator) }

    override fun create() {
        Bullet.init()
        setScreen(gameScreen)
        Gdx.input.inputProcessor = gameScreen
    }

    override fun dispose() {
        super.dispose()
        gameScreen.dispose()
    }

}