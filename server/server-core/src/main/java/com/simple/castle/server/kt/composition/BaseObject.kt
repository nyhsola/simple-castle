package com.simple.castle.server.kt.composition

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import com.simple.castle.server.kt.physic.PhysicObject
import java.util.*

@Suppress("LeakingThis")
open class BaseObject(constructor: Constructor) : Disposable {
    val nodeName: String = constructor.nodeName
    val id: String = UUID.randomUUID().toString()
    val physicObject: PhysicObject = constructor.buildPhysic()
    val modelInstance: ModelInstance = constructor.buildModel()
    var hide: Boolean

    init {
        physicObject.motionState.transform = modelInstance.transform
        physicObject.body.motionState = physicObject.motionState
        hide = constructor.hide
        if (constructor.mass != 0.0f) {
            physicObject.body.collisionShape.calculateLocalInertia(constructor.mass, Vector3.Zero)
        }
        physicObject.body.userData = this
    }

    override fun dispose() {
        physicObject.dispose()
    }
}