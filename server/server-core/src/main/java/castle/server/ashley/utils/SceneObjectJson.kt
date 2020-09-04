package castle.server.ashley.utils

import castle.server.ashley.physic.InteractType
import castle.server.ashley.physic.PhysicShape


data class SceneObjectJson(var nodes: String = "",
                           var shape: PhysicShape = PhysicShape.STATIC,
                           var mass: Float = 0.0f,
                           var interact: InteractType = InteractType.GHOST,
                           var instantiate: Boolean = false,
                           var hide: Boolean = false,
                           var armature: String = "",
                           var animation: String = "")