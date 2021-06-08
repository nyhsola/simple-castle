package castle.server.ashley.utils.physic

import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.model.Node
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape
import java.util.function.Function
import kotlin.math.max


enum class PhysicShape(private val function: Function<ModelInstance, btCollisionShape>) {
    STATIC(Function<ModelInstance, btCollisionShape> { node -> calculateStaticNodeShape(node) }), BASE_BOX(Function<ModelInstance, btCollisionShape> { node ->
        calculateBaseBox(
            node
        )
    }),
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
            val temp = BoundingBox()
            val boundingBox = modelInstance.calculateBoundingBox(temp)
            val dimensions = Vector3()
            boundingBox.getDimensions(dimensions)
            val max = max(dimensions.x, dimensions.z)
            return btBoxShape(Vector3(max, dimensions.y, max).scl(SCALAR))
        }

        private fun calculateAdjustedBox(modelInstance: ModelInstance): btBoxShape {
            val temp = BoundingBox()
            val boundingBox = modelInstance.calculateBoundingBox(temp)
            val dimensions = Vector3()
            boundingBox.getDimensions(dimensions)
            return btBoxShape(dimensions.scl(SCALAR))
        }

        private fun calculateDynamic(node: Node): btCollisionShape {
            val mesh: Mesh = node.parts[0].meshPart.mesh
            return btConvexHullShape(mesh.verticesBuffer, mesh.numVertices, mesh.vertexSize)
        }
    }
}