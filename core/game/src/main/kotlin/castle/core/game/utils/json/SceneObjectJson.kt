package castle.core.game.utils.json

import castle.core.physic.PhysicShape

data class SceneObjectJson(
    var nodes: String = "",
    var shape: PhysicShape = PhysicShape.STATIC,
    var mass: Float = 0.0f,
    var collisionFilterGroup: Int = 1,
    var collisionFilterMask: List<Int> = listOf(1),
    var collisionFlag: List<String> = emptyList(),
    var instantiate: Boolean = false,
    var hide: Boolean = false,
    var armature: String = "",
    var animation: String = ""
)