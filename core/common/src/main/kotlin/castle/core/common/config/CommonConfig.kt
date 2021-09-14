package castle.core.common.config

import castle.core.common.service.CameraService
import castle.core.common.system.*

class CommonConfig {
    val cameraService = CameraService()
    val cameraControlSystem = CameraControlSystem(cameraService)
    val stageRectRenderSystem = StageRenderSystem()
    private val animationSystem = AnimationSystem()
    val systems = listOf(cameraControlSystem, stageRectRenderSystem, animationSystem)
}