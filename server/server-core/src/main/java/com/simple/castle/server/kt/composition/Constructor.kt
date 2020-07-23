package com.simple.castle.server.kt.composition

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.simple.castle.server.kt.physic.PhysicObject

class Constructor(private val model: Model,
                  val id: String,
                  private val interactType: InteractType,
                  val mass: Float,
                  private val physicShape: PhysicShape,
                  val instantiate: Boolean,
                  val hide: Boolean) {
    fun buildPhysic(): PhysicObject {
        val node = model.getNode(id)
        val shape = physicShape.build(node)
        val info: btRigidBody.btRigidBodyConstructionInfo = btRigidBody.btRigidBodyConstructionInfo(mass, null, shape)
        return interactType.build(info)
    }

    fun buildModel(): ModelInstance {
        return ModelInstance(model, id, true)
    }
}