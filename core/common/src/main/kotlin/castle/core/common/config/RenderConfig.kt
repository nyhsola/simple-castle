package castle.core.common.config

import castle.core.common.system.Line3DRenderSystem
import castle.core.common.system.ModelRenderSystem
import castle.core.common.system.Rect3DRenderSystem

class RenderConfig(guiConfig: GUIConfig, commonConfig: CommonConfig) {
    private val modelRenderSystem = ModelRenderSystem(guiConfig, commonConfig.cameraService)
    private val rect3DRenderSystem = Rect3DRenderSystem(guiConfig, commonConfig.cameraService)
    private val line3DRenderSystem = Line3DRenderSystem(guiConfig, commonConfig.cameraService)
    val systems = listOf(modelRenderSystem, rect3DRenderSystem, line3DRenderSystem)
}