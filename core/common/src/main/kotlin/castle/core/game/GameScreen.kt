package castle.core.game

import castle.core.config.ScreenConfig
import castle.core.system.*
import castle.core.system.render.*
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import ktx.app.KtxScreen
import org.koin.core.annotation.Single

@Single
class GameScreen(
    engine: PooledEngine,
    stageRenderSystem: StageRenderSystem,
    animationRenderSystem: AnimationRenderSystem,
    modelRenderSystem: ModelRenderSystem,
    lineRenderSystem: LineRenderSystem,
    circleRenderSystem: CircleRenderSystem,
    textRenderSystem: TextRenderSystem,
    hpRenderSystem: HpRenderSystem,
    stateSystem: StateSystem,
    physicSystem: PhysicSystem,
    unitSystem: UnitSystem,
    mapSystem: MapSystem,
    gameManagerSystem: GameManagerSystem
) : KtxScreen {
    private val screenConfig = ScreenConfig(
        engine,
        linkedSetOf(
            animationRenderSystem,
            modelRenderSystem,
            circleRenderSystem,
            lineRenderSystem,
            textRenderSystem,
            hpRenderSystem,
            stageRenderSystem,
            physicSystem,
            stateSystem,
            unitSystem,
            mapSystem,
            gameManagerSystem
        )
    )

    private val engine = screenConfig.engine
    private val inputMultiplexer = screenConfig.inputMultiplexer
    private val screens = screenConfig.screens
    private val disposables = screenConfig.disposables

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