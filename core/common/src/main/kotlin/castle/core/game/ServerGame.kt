package castle.core.game

import castle.core.config.GameModule
import castle.core.event.EventContext
import castle.core.event.EventQueue
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.physics.bullet.Bullet
import ktx.app.KtxGame
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.min

class ServerGame : KtxGame<Screen>(), KoinComponent {
    private val gameModule = GameModule()

    private val eventQueue: EventQueue by inject()
    private val startScreen: StartScreen by inject()
    private val gameScreen: GameScreen by inject()

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(StartScreen.GAME_EVENT) { setScreen<GameScreen>() }
    )

    init {
        Bullet.init(false, false)
    }

    override fun create() {
        gameModule.start()
        addScreen(startScreen)
        addScreen(gameScreen)
        setScreen<StartScreen>()
    }

    override fun render() {
        eventQueue.proceed(operations)
        currentScreen.render(min(1f / 30f, Gdx.graphics.deltaTime))
    }

    override fun dispose() {
        gameModule.dispose()
        super.dispose()
    }
}