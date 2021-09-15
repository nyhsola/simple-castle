package castle.core.common.config

import castle.core.common.system.Line3DRenderSystem
import castle.core.common.system.ModelRenderSystem

class RenderConfig(guiConfig: GUIConfig, commonConfig: CommonConfig) {
    private val modelRenderSystem = ModelRenderSystem(guiConfig, commonConfig.cameraService)
    private val line3DRenderSystem = Line3DRenderSystem(guiConfig, commonConfig.cameraService)
    val systems = listOf(modelRenderSystem, line3DRenderSystem)
}