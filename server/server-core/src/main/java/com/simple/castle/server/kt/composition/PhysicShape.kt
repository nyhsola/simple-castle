package com.simple.castle.server.kt.composition

import com.badlogic.gdx.graphics.g3d.model.Node
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import java.util.function.Function

enum class PhysicShape(private val function: Function<Node, btCollisionShape>) {
    STATIC(Function<Node, btCollisionShape> { node: Node -> calculateStaticNodeShape(node) }),
    BASE_BOX(Function<Node, btCollisionShape> { node: Node -> calculateBaseBox(node) }),
    ADJUSTED_BASE_BOX(Function<Node, btCollisionShape> { node: Node -> calculateAdjustedBox(node) });

    fun build(node: Node): btCollisionShape {
        return function.apply(node)
    }

    companion object {
        private const val SCALAR = 0.5f
        private fun calculateStaticNodeShape(node: Node): btCollisionShape {
            return Bullet.obtainStaticNodeShape(node, false)
        }

        private fun calculateBaseBox(node: Node): btBoxShape {
            val temp = BoundingBox()
            val boundingBox = node.calculateBoundingBox(temp)
            val dimensions = Vector3()
            boundingBox.getDimensions(dimensions)
            val max = Math.max(dimensions.x, dimensions.z)
            return btBoxShape(Vector3(max, dimensions.y, max).scl(SCALAR))
        }

        private fun calculateAdjustedBox(node: Node): btBoxShape {
            val temp = BoundingBox()
            val boundingBox = node.calculateBoundingBox(temp)
            val dimensions = Vector3()
            boundingBox.getDimensions(dimensions)
            return btBoxShape(dimensions.scl(SCALAR))
        }
    }
}