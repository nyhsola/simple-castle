package castle.core.common.physic

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import java.util.function.Function
import kotlin.math.max

enum class PhysicShape(private val function: Function<ModelInstance, btCollisionShape>) {
    STATIC(Function<ModelInstance, btCollisionShape> { node -> calculateStaticNodeShape(node) }),
    BASE_BOX(Function<ModelInstance, btCollisionShape> { node -> calculateBaseBox(node) }),
    ADJUSTED_BASE_BOX(Function<ModelInstance, btCollisionShape> { node -> calculateAdjustedBox(node) });

    fun build(modelInstance: ModelInstance): btCollisionShape {
        return function.apply(modelInstance)
    }

    companion object {
        private const val SCALAR = 0.5f
        private fun calculateStaticNodeShape(modelInstance: ModelInstance): btCollisionShape {
            return Bullet.obtainStaticNodeShape(modelInstance.nodes[0], false)
        }

        private fun calculateBaseBox(modelInstance: ModelInstance): btBoxShape {
            val dimensions = getDimension(modelInstance)
            val max = max(dimensions.x, dimensions.z)
            return btBoxShape(Vector3(max, dimensions.y, max).scl(SCALAR))
        }

        private fun calculateAdjustedBox(modelInstance: ModelInstance): btBoxShape {
            val dimensions = getDimension(modelInstance)
            return btBoxShape(dimensions.scl(SCALAR))
        }

        private fun getDimension(modelInstance: ModelInstance) = modelInstance.calculateBoundingBox(BoundingBox()).getDimensions(Vector3())
    }
}