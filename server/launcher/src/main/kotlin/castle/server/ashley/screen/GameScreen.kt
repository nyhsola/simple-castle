package castle.server.ashley.screen

import castle.core.common.builder.ScreenConfigurator
import castle.core.common.config.CommonConfig
import castle.core.common.config.GUIConfig
import castle.core.game.config.GameConfig
import castle.core.common.config.PhysicConfig
import castle.core.common.config.RenderConfig
import com.badlogic.gdx.Gdx
import ktx.app.KtxScreen

class GameScreen : KtxScreen {
    private val commonConfig = CommonConfig()
    private val guiConfig = GUIConfig()
    private val physicConfig = PhysicConfig(commonConfig)
    private val renderConfig = RenderConfig(guiConfig, commonConfig)
    private val gameConfig = GameConfig(guiConfig, commonConfig, physicConfig)

    private val screenConfigurator = ScreenConfigurator(
        listOf(
            commonConfig.cameraControlSystem,
            renderConfig.modelRenderSystem,
            renderConfig.rect3DRenderSystem,
            renderConfig.line3DRenderSystem,
            commonConfig.stageRectRenderSystem,
            physicConfig.physicSystem,
            commonConfig.animationSystem,
            gameConfig.gameCycleSystem
        )
    )

    private val engine = screenConfigurator.engine
    private val inputMultiplexer = screenConfigurator.inputMultiplexer
    private val disposables = screenConfigurator.disposables

    override fun resize(width: Int, height: Int) {
        commonConfig.cameraControlSystem.resize(width, height)
        commonConfig.stageRectRenderSystem.resize(width, height)
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
        guiConfig.dispose()
        engine.removeAllEntities()
        disposables.forEach { it.dispose() }
    }
}