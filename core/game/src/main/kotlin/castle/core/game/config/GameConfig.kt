package castle.core.game.config

import castle.core.common.config.CommonConfig
import castle.core.common.config.GUIConfig
import castle.core.common.config.PhysicConfig
import castle.core.game.system.*
import castle.core.game.system.GameCycleSystem
import com.badlogic.gdx.utils.Disposable

class GameConfig(
    guiConfig: GUIConfig,
    commonConfig: CommonConfig,
    physicConfig: PhysicConfig
) : Disposable {
    private val gameInternalConfig = GameInternalConfig(commonConfig, physicConfig, guiConfig)

    private val gameCycleSystem = GameCycleSystem(gameInternalConfig)
    private val hpSystem = HPSystem(guiConfig)
    private val pathSystem = PathSystem(gameInternalConfig.mapService)
    private val moveSystem = MoveSystem()
    private val mapSystem = MapSystem(gameInternalConfig.mapService, gameInternalConfig.gameUI.minimap)
    private val behaviourSystem = BehaviourSystem()
    private val attackSystem = AttackSystem(gameInternalConfig.mapService)
    private val playerSystem = PlayerSystem(gameInternalConfig.gameResourceService)
    private val textSystem = TextSystem(guiConfig, gameInternalConfig.gameResourceService, gameInternalConfig.commonConfig.cameraService)
    private val startupSystem = StartupSystem(gameInternalConfig.gameResourceService)

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
            startupSystem
        )

    override fun dispose() {
        gameInternalConfig.dispose()
    }
}