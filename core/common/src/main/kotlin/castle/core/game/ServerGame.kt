package castle.core.game

import castle.core.config.CommonConfig
import castle.core.config.GameConfig
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.physics.bullet.Bullet
import ktx.app.KtxGame
import kotlin.math.min

class ServerGame : KtxGame<Screen>() {
    private val commonConfig by lazy { CommonConfig() }
    private val gameConfig by lazy { GameConfig(commonConfig) }

    init {
        Bullet.init(false, false)
    }

    override fun create() {
        addScreen(StartScreen(commonConfig))
        addScreen(GameScreen(gameConfig))
        setScreen<StartScreen>()
    }

    override fun render() {
        proceedEvent()
        currentScreen.render(min(1f / 30f, Gdx.graphics.deltaTime))
    }

    override fun dispose() {
        commonConfig.dispose()
        gameConfig.dispose()
        super.dispose()
    }

    private fun proceedEvent() {
        commonConfig.eventQueue.proceed {
            when (it.eventType) {
                StartScreen.GAME_EVENT -> {
                    setScreen<GameScreen>()
                }
            }
            false
        }
    }
}