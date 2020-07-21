package com.simple.castle.server.loader.json

import com.simple.castle.server.kt.composition.InteractType
import com.simple.castle.server.kt.composition.PhysicShape

class SceneObjectJson {
    var nodePattern: String? = null
    var shape: PhysicShape? = null
    var mass: Float? = null
    var interact: InteractType? = null
    var instantiate: Boolean? = null
    var hide: Boolean? = null

    constructor() {}
    constructor(sceneObjectJson: SceneObjectJson?) {
        nodePattern = sceneObjectJson!!.nodePattern
        shape = sceneObjectJson.shape
        mass = sceneObjectJson.mass
        interact = sceneObjectJson.interact
        instantiate = sceneObjectJson.instantiate
        hide = sceneObjectJson.hide
    }

}