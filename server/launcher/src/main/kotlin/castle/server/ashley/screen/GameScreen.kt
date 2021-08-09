package castle.server.ashley.screen

import castle.core.common.builder.ScreenConfigurator
import castle.core.common.config.CommonConfig
import castle.core.common.creator.GUIConfig
import castle.core.game.config.GameConfig
import castle.core.physic.config.PhysicConfig
import castle.core.game.system.GameCycleSystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import ktx.app.KtxScreen

class GameScreen(guiConfig: GUIConfig) : KtxScreen {
    private val commonConfig = CommonConfig(guiConfig)
    private val physicConfig = PhysicConfig(commonConfig)
    private val gameConfig = GameConfig(guiConfig, commonConfig, physicConfig)

    private val cameraService = commonConfig.cameraService

    private val screenConfigurator = ScreenConfigurator(
        listOf(
            commonConfig.cameraControlSystem,
            commonConfig.modelRenderSystem,
            commonConfig.rectRenderSystem,
            commonConfig.line3DRenderSystem,
            commonConfig.stageRectRenderSystem,
            physicConfig.physicSystem,
            commonConfig.animationSystem,
            gameConfig.gameCycleSystem
        )
    )

    private val engine: PooledEngine = screenConfigurator.engine
    private val inputMultiplexer = screenConfigurator.inputMultiplexer
    private val disposables = screenConfigurator.disposables

    override fun resize(width: Int, height: Int) {
        cameraService.resize(width, height)
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
    }
}