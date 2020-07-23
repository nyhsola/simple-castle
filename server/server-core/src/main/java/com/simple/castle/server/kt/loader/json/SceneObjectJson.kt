package com.simple.castle.server.kt.loader.json

import com.simple.castle.server.kt.composition.InteractType
import com.simple.castle.server.kt.composition.PhysicShape


data class SceneObjectJson(var nodePattern: String = "",
                           var shape: PhysicShape = PhysicShape.STATIC,
                           var mass: Float = 0.0f,
                           var interact: InteractType = InteractType.GHOST,
                           var instantiate: Boolean = false,
                           var hide: Boolean = true)