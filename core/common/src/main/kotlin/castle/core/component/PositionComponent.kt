package castle.core.component

import castle.core.util.ModelUtils.Companion.DEFAULT_FACE_DIRECTION
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

class PositionComponent(
    val nodeName: String,
    val matrix4: Matrix4
) : Component {
    companion object {
        val mapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)
        private val tempVector1: Vector3 = Vector3()
        private val tempOrientation: Quaternion = Quaternion()
        private val tempDefaultFace: Vector3 = Vector3()
        private val tempAngle: Quaternion = Quaternion()
    }

    val faceDirection: Float
        get() {
            val orientation = matrix4.getRotation(tempOrientation)
            val faceDirection = orientation.transform(tempDefaultFace.set(DEFAULT_FACE_DIRECTION))
            return tempAngle.setFromCross(DEFAULT_FACE_DIRECTION, faceDirection).angle
        }

    fun setMatrix(positionComponent: PositionComponent) {
        matrix4.set(positionComponent.matrix4)
    }

    fun rotateTo(positionComponent: PositionComponent) {
        val nextPosition = positionComponent.matrix4.getTranslation(tempVector1)
        matrix4.rotateTowardTarget(nextPosition, Vector3.Y)
    }
}