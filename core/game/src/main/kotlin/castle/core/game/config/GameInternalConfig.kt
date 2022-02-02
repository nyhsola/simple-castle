package castle.core.game.config

import castle.core.common.config.CommonConfig
import castle.core.common.config.GUIConfig
import castle.core.common.config.PhysicConfig
import castle.core.game.builder.PlayerBuilder
import castle.core.game.builder.UnitBuilder
import castle.core.game.service.*
import castle.core.game.ui.debug.DebugUI
import castle.core.game.ui.game.GameUI
import com.badlogic.gdx.utils.Disposable

class GameInternalConfig(
    val commonConfig: CommonConfig,
    physicConfig: PhysicConfig,
    guiConfig: GUIConfig
) : Disposable {
    val gameResources = GameResources()
    val neutralInitService = NeutralInitService(commonConfig.environmentBuilder, commonConfig.commonResources)
    private val unitBuilder = UnitBuilder(commonConfig.commonResources, commonConfig.templateBuilder)
    val playerBuilder = PlayerBuilder(gameResources, neutralInitService, unitBuilder)
    val playerInitService = PlayerInitService(gameResources, playerBuilder)
    val rayCastService = RayCastService(physicConfig.physicService, commonConfig.cameraService)
    val mapService = MapService(physicConfig.scanService)
    val gameUI = GameUI(physicConfig.scanService, commonConfig.commonResources, guiConfig, commonConfig.eventQueue)
    private val debugUI = DebugUI(commonConfig.commonResources, guiConfig, commonConfig.eventQueue)
    private val chatService = ChatService(gameUI.chat)
    val uiService = UIService(commonConfig.eventQueue, commonConfig.commonResources, gameUI, debugUI, chatService)
    val selectionService = SelectionService(uiService)

    override fun dispose() {
        gameUI.dispose()
        debugUI.dispose()
    }
}