package castle.core.common.config

import castle.core.common.service.PhysicService
import castle.core.common.service.ScanService
import castle.core.common.system.PhysicSystem

class PhysicConfig(commonConfig: CommonConfig) {
    val physicService = PhysicService(commonConfig.cameraService)
    val scanService = ScanService(commonConfig.commonResources, commonConfig.templateBuilder, physicService)
    private val physicSystem = PhysicSystem(physicService, commonConfig.eventQueue)
    val systems = listOf(physicSystem)
}