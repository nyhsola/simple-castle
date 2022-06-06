package castle.core.game

import castle.core.config.GameConfig
import com.badlogic.gdx.Gdx
import ktx.app.KtxScreen

class GameScreen(gameConfig: GameConfig) : KtxScreen {
    private val engine = gameConfig.screenConfig.engine
    private val inputMultiplexer = gameConfig.screenConfig.inputMultiplexer
    private val screens = gameConfig.screenConfig.screens
    private val disposables = gameConfig.screenConfig.disposables

    override fun resize(width: Int, height: Int) {
        screens.forEach { it.resize(width, height) }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun show() {
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }
    }
}