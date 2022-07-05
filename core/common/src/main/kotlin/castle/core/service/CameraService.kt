package castle.core.service

import castle.core.component.PositionComponent
import castle.core.`object`.GameCamera
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import ktx.app.KtxInputAdapter
import org.koin.core.annotation.Single

@Single
class CameraService : KtxInputAdapter {
    private val tempVector = Vector3()
    private val index = 0
    private val cameras = listOf(GameCamera())

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

    fun update(deltaTime: Float) {
        currentCamera.update(deltaTime)
    }

    fun resize(width: Int, height: Int) {
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