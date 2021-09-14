package castle.core.common.config

import castle.core.common.service.PhysicService
import castle.core.common.system.PhysicSystem

class PhysicConfig(commonConfig: CommonConfig) {
    val physicService = PhysicService(commonConfig.cameraService)
    private val physicSystem = PhysicSystem(physicService)
    val systems = listOf(physicSystem)
}