package castle.core.screen

import castle.core.config.GameConfig
import castle.core.screen.add.ScreenConfigurator
import com.badlogic.gdx.Gdx
import ktx.app.KtxScreen

class GameScreen : KtxScreen {
    private val gameConfig = GameConfig()
    private val screenConfigurator = ScreenConfigurator(gameConfig.systems)
    private val engine = screenConfigurator.engine
    private val inputMultiplexer = screenConfigurator.inputMultiplexer
    private val screens = screenConfigurator.screens
    private val disposables = screenConfigurator.disposables

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
        engine.removeAllEntities()
        disposables.forEach { it.dispose() }
        gameConfig.dispose()
    }
}