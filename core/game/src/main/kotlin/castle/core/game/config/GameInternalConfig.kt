package castle.core.game.config

import castle.core.common.config.CommonConfig
import castle.core.common.config.GUIConfig
import castle.core.common.config.PhysicConfig
import castle.core.game.service.GameResourceService
import castle.core.game.service.MapService
import castle.core.game.service.RayCastService
import castle.core.game.service.ScanService
import castle.core.game.ui.debug.DebugUI
import castle.core.game.ui.game.GameUI
import com.badlogic.gdx.utils.Disposable

class GameInternalConfig(
    val commonConfig: CommonConfig,
    val physicConfig: PhysicConfig,
    guiConfig: GUIConfig
) : Disposable {
    val gameResourceService = GameResourceService()
    val rayCastService = RayCastService(physicConfig.physicService, commonConfig.cameraService)
    private val scanService = ScanService(gameResourceService, physicConfig.physicService)
    val mapService = MapService(scanService)
    val gameUI = GameUI(scanService, gameResourceService, guiConfig)
    val debugUI = DebugUI(gameResourceService, guiConfig)

    override fun dispose() {
        gameUI.dispose()
        debugUI.dispose()
        gameResourceService.dispose()
    }
}