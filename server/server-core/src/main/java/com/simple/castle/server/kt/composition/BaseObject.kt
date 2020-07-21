package com.simple.castle.server.kt.composition

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.utils.Disposable
import com.simple.castle.server.physic.unit.PhysicObject

open class BaseObject(constructor: Constructor) : Disposable {
    private val id: String?
    val physicObject: PhysicObject?
    val modelInstance: ModelInstance?
    var hide: Boolean?

    override fun dispose() {
        physicObject!!.dispose()
    }

    init {
        id = constructor.id
        physicObject = constructor.buildPhysic()
        modelInstance = constructor.buildModel()
        physicObject!!.motionState.setWorldTransform(modelInstance.transform)
        hide = constructor.hide
    }
}