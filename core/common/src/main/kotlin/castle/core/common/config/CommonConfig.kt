package castle.core.common.config

import castle.core.common.creator.GUIConfig
import castle.core.common.service.CameraService
import castle.core.common.system.*

class CommonConfig(guiConfig: GUIConfig) {
    val cameraService = CameraService()
    val cameraControlSystem = CameraControlSystem(cameraService)
    val modelRenderSystem = ModelRenderSystem(guiConfig, cameraService)
    val rectRenderSystem = RectRenderSystem(guiConfig)
    val line3DRenderSystem = Line3DRenderSystem(guiConfig, cameraService)
    val stageRectRenderSystem = StageRenderSystem()
    val animationSystem = AnimationSystem()
}