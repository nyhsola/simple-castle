package com.simple.castle.server.kt.composition

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.simple.castle.server.kt.physic.PhysicObject

class Constructor(private val model: Model, val id: String, private val interactType: InteractType, private val physicShape: PhysicShape, val instantiate: Boolean, val hide: Boolean) {
    fun buildPhysic(): PhysicObject? {
        val node = model.getNode(id)
        val shape = physicShape.build(node)
        val info: btRigidBody.btRigidBodyConstructionInfo = btRigidBody.btRigidBodyConstructionInfo(0.toFloat(), null, shape)
        return interactType.build(info)
    }

    fun buildModel(): ModelInstance {
        return ModelInstance(model, id, true)
    }

}