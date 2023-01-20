package castle.core.physic

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import kotlin.math.max

class PhysicShapeInit {
    companion object {
        private const val SCALAR = 0.5f
        fun calculateStaticNodeShape(modelInstance: ModelInstance): btCollisionShape = Bullet.obtainStaticNodeShape(modelInstance.nodes[0], false)

        fun calculateBaseBox(modelInstance: ModelInstance): btBoxShape {
            val dimensions = getDimension(modelInstance)
            val max = max(dimensions.x, dimensions.z)
            return btBoxShape(Vector3(max, dimensions.y, max).scl(SCALAR))
        }

        fun calculateAdjustedBox(modelInstance: ModelInstance) = btBoxShape(getDimension(modelInstance).scl(SCALAR))

        private fun getDimension(modelInstance: ModelInstance) = modelInstance.calculateBoundingBox(BoundingBox()).getDimensions(Vector3())
    }
}