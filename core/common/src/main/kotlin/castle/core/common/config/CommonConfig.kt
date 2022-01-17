package castle.core.common.config

import castle.core.common.builder.EnvironmentBuilder
import castle.core.common.builder.TemplateBuilder
import castle.core.common.event.EventQueue
import castle.core.common.service.CameraService
import castle.core.common.service.CommonResources
import castle.core.common.system.AnimationSystem
import castle.core.common.system.CameraControlSystem
import castle.core.common.system.StageRenderSystem
import com.badlogic.gdx.utils.Disposable

class CommonConfig : Disposable {
    val eventQueue = EventQueue()
    val cameraService = CameraService()
    val cameraControlSystem = CameraControlSystem(cameraService, eventQueue)
    val stageRectRenderSystem = StageRenderSystem()
    val commonResources = CommonResources()
    val templateBuilder = TemplateBuilder(commonResources)
    val environmentBuilder = EnvironmentBuilder(commonResources, templateBuilder)
    private val animationSystem = AnimationSystem()
    val systems = listOf(cameraControlSystem, stageRectRenderSystem, animationSystem)

    override fun dispose() {
        commonResources.dispose()
    }
}