package castle.server.ashley.builder

import castle.core.common.system.*
import castle.core.game.system.*

class Priority {
    companion object {
        val order = linkedSetOf(
            BaseCommandsSystem::class.java,
            SelectionSystem::class.java,
            AnimationSystem::class.java,
            CameraControlSystem::class.java,
            ModelRenderSystem::class.java,
            Circle3DRenderSystem::class.java,
            Line3DRenderSystem::class.java,
            TextSystem::class.java,
            TextCountSystem::class.java,
            PhysicSystem::class.java,
            HPSystem::class.java,
            StageRenderSystem::class.java,
            AttackSystem::class.java,
            BehaviourSystem::class.java,
            UISystem::class.java,
            MapSystem::class.java,
            MoveSystem::class.java,
            PathSystem::class.java,
            PlayerSystem::class.java,
            StartupSystem::class.java
        )
    }
}