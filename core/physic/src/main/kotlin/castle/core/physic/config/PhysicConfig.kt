package castle.core.physic.config

import castle.core.common.config.CommonConfig
import castle.core.physic.service.PhysicService
import castle.core.physic.system.PhysicSystem

class PhysicConfig(
    commonConfig: CommonConfig
) {
    val physicService = PhysicService(commonConfig.cameraService)
    val physicSystem = PhysicSystem(physicService)
}