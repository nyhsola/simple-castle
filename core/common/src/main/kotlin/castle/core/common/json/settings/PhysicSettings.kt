package castle.core.common.json.settings

import castle.core.common.physic.PhysicShape

data class PhysicSettings(
    val enabled: Boolean = true,
    val shape: PhysicShape = PhysicShape.STATIC,
    val mass: Float = 0.0f,
    val collisionFilterGroup: Int = 1,
    val collisionFilterMask: List<Int> = listOf(1),
    val collisionFlag: List<String> = emptyList()
)