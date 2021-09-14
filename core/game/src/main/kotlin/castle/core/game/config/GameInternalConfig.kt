package castle.core.game.config

import castle.core.common.config.CommonConfig
import castle.core.common.config.GUIConfig
import castle.core.common.config.PhysicConfig
import castle.core.game.`object`.GameEnvironment
import castle.core.game.ui.GameUI
import castle.core.game.service.GameResourceService
import castle.core.game.service.MapService
import castle.core.game.service.RayCastService
import castle.core.game.service.ScanService
import com.badlogic.gdx.utils.Disposable

internal class GameInternalConfig(
    val commonConfig: CommonConfig,
    val physicConfig: PhysicConfig,
    guiConfig: GUIConfig
) : Disposable {
    val gameResourceService = GameResourceService()
    val gameEnvironment = GameEnvironment(gameResourceService)
    val rayCastService = RayCastService(physicConfig.physicService, commonConfig.cameraService)
    private val scanService = ScanService(gameResourceService, physicConfig.physicService)
    val mapService = MapService(scanService)
    val gameUI = GameUI(scanService, gameResourceService, guiConfig)

    override fun dispose() {
        gameUI.dispose()
        gameEnvironment.dispose()
        gameResourceService.dispose()
    }
}