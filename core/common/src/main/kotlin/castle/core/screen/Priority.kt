package castle.core.screen

import castle.core.system.GameManagerSystem
import castle.core.system.PhysicSystem
import castle.core.system.TextCountSystem
import castle.core.system.UnitSystem
import castle.core.system.render.*

class Priority {
    companion object {
        val order = linkedSetOf(
                AnimationRenderSystem::class.java,
                ModelRenderSystem::class.java,
                CircleRenderSystem::class.java,
                LineRenderSystem::class.java,
                TextRenderSystem::class.java,
                TextCountSystem::class.java,
                PhysicSystem::class.java,
                HpRenderSystem::class.java,
                StageRenderSystem::class.java,
                UnitSystem::class.java,
                GameManagerSystem::class.java
        )
    }
}