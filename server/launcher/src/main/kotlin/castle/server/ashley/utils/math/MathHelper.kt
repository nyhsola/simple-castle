package castle.server.ashley.utils.math

import com.badlogic.gdx.math.Vector3
import kotlin.math.acos

class MathHelper {
    companion object {
        fun getAngle(a: Vector3, b: Vector3): Double {
            val aNor = a.cpy().nor()
            val bNor = b.cpy().nor()
            return Math.toDegrees(acos(aNor.dot(bNor).toDouble()))
        }
    }
}