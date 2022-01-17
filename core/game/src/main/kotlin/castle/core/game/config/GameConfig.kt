package castle.core.game.config

import castle.core.common.config.CommonConfig
import castle.core.common.config.GUIConfig
import castle.core.common.config.PhysicConfig
import castle.core.game.system.*
import com.badlogic.gdx.utils.Disposable

class GameConfig(
    guiConfig: GUIConfig,
    commonConfig: CommonConfig,
    physicConfig: PhysicConfig
) : Disposable {
    private val gameInternalConfig = GameInternalConfig(commonConfig, physicConfig, guiConfig)
    private val gameCycleSystem = GameCycleSystem(commonConfig.eventQueue, gameInternalConfig)
    private val hpSystem = HPSystem(guiConfig)
    private val pathSystem = PathSystem(gameInternalConfig.neutralInitService, gameInternalConfig.mapService)
    private val moveSystem = MoveSystem()
    private val mapSystem = MapSystem(gameInternalConfig.mapService, gameInternalConfig.gameUI.minimap)
    private val behaviourSystem = BehaviourSystem()
    private val attackSystem = AttackSystem(gameInternalConfig.mapService)
    private val playerSystem = PlayerSystem(commonConfig.eventQueue, gameInternalConfig.playerBuilder)
    private val textSystem = TextSystem(guiConfig, gameInternalConfig.gameResources, gameInternalConfig.commonConfig.cameraService)
    private val textCountSystem = TextCountSystem()
    private val startupSystem = StartupSystem(gameInternalConfig.neutralInitService, gameInternalConfig.playerInitService)

    val systems =
        linkedSetOf(
            gameCycleSystem,
            hpSystem,
            moveSystem,
            pathSystem,
            mapSystem,
            behaviourSystem,
            attackSystem,
            playerSystem,
            textSystem,
            textCountSystem,
            startupSystem
        )

    override fun dispose() {
        gameInternalConfig.dispose()
    }
}