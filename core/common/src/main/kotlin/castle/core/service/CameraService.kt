package castle.core.service

import castle.core.component.PositionComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.`object`.GameCamera
import castle.core.service.abs.Service
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class CameraService(
    private val eventQueue: EventQueue
) : Service {
    companion object {
        const val CHAT_FOCUSED = "CHAT_FOCUSED"
        const val CHAT_UNFOCUSED = "CHAT_UNFOCUSED"
    }

    private val tempVector = Vector3()
    private val index = 0
    private val cameras = listOf(GameCamera())

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(CHAT_FOCUSED) { input = false },
        Pair(CHAT_UNFOCUSED) { input = true },
    )

    val currentCamera
        get() = cameras[index]

    var input: Boolean = true
        set(value) {
            currentCamera.resetKeys()
            field = value
        }

    fun set(entity: Entity) {
        val unitPosition = PositionComponent.mapper.get(entity).matrix4.getTranslation(tempVector)
        val cameraPosition = currentCamera.camera.position
        cameraPosition.set(unitPosition.x + 25f, cameraPosition.y, unitPosition.z)
        currentCamera.camera.update()
    }

    override fun update(deltaTime: Float) {
        currentCamera.update(deltaTime)
    }

    override fun updateTick(deltaTime: Float) {
        eventQueue.proceed(operations)
    }

    override fun resize(width: Int, height: Int) {
        currentCamera.resize(width, height)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return if (input) currentCamera.scrolled(amountY) else false
    }

    override fun keyUp(keycode: Int): Boolean {
        return if (input) currentCamera.keyUp(keycode) else false
    }

    override fun keyDown(keycode: Int): Boolean {
        return if (input) currentCamera.keyDown(keycode) else false
    }
}