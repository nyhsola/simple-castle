package castle.core.game.config

import castle.core.common.config.CommonConfig
import castle.core.common.config.GUIConfig
import castle.core.game.system.GameCycleSystem
import castle.core.common.config.PhysicConfig

class GameConfig(
    guiConfig: GUIConfig,
    commonConfig: CommonConfig,
    physicConfig: PhysicConfig
) {
    val gameCycleSystem = GameCycleSystem(guiConfig, physicConfig.physicService, commonConfig.cameraService)
}