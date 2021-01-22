package castle.server.ashley.systems

import castle.server.ashley.service.CameraService
import castle.server.ashley.systems.adapter.SystemInputAdapter
import com.badlogic.ashley.core.Engine

class CameraControlSystem(private val cameraService: CameraService) : SystemInputAdapter() {
    override fun addedToEngine(engine: Engine) {
        addInputProcessor(cameraService)
        super.addedToEngine(engine)
    }

    override fun update(deltaTime: Float) {
        cameraService.update(deltaTime)
    }
}