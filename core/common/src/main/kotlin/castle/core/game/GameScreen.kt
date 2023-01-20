package castle.core.game

import castle.core.system.*
import castle.core.system.render.*
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import org.koin.core.annotation.Single

@Single
class GameScreen(
        private val engine: PooledEngine,
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
        removeSystem: RemoveSystem,
        gameManagerSystem: GameManagerSystem
) : KtxScreen {
    private val entitySystems = linkedSetOf(
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
            removeSystem,
            gameManagerSystem
    )

    private val inputMultiplexer = InputMultiplexer()
    private val screens = ArrayList<KtxScreen>(entitySystems.filter { it is KtxScreen }.map { it as KtxScreen })
    private val disposables = ArrayList(entitySystems.filter { it is Disposable }.map { it as Disposable }.reversed())

    init {
        entitySystems
                .filter { it is KtxInputAdapter }
                .forEach { inputMultiplexer.addProcessor(it as KtxInputAdapter) }

        entitySystems.forEachIndexed { index, entitySystem ->
            entitySystem.priority = index
            engine.addSystem(entitySystem)
        }
    }

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