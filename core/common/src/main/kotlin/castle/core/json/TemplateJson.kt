package castle.core.json

import castle.core.physic.PhysicShape

data class TemplateJson(
        val templateName: String = "",
        val hide: Boolean = false,
        val shape: PhysicShape = PhysicShape.STATIC,
        val mass: Float = 0.0f,
        val collisionFilterGroup: Int = 1,
        val collisionFilterMask: List<Int> = listOf(1),
        val collisionFlag: List<String> = emptyList()
)