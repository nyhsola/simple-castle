package castle.core.game.system

import castle.core.common.component.PhysicComponent
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.game.builder.UnitBuilder
import castle.core.game.component.MoveComponent
import castle.core.game.service.ChatService
import castle.core.game.service.GameResources
import castle.core.game.service.RayCastService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import ktx.app.KtxInputAdapter

class ClickSystem(
    private val rayCastService: RayCastService,
    private val chatService: ChatService,
    unitBuilder: UnitBuilder,
    gameResources: GameResources
) : EntitySystem(), KtxInputAdapter {
    private var currentPick: Entity = unitBuilder.build(gameResources.units.getValue("pick"))

    init {
        RenderComponent.mapper.get(currentPick).apply { hide = true }
    }

    override fun addedToEngine(engine: Engine) {
        engine.addEntity(currentPick)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val collisionObject = rayCastService.rayCast(screenX.toFloat(), screenY.toFloat())
        val node = (collisionObject?.userData ?: "") as String
        val unit = getEntity(node)

        val renderComponent = RenderComponent.mapper.get(currentPick)
        if (unit == null) {
            renderComponent.apply { hide = true }
            return false
        }

        renderComponent.apply { hide = false }
        val positionCurrentPick = PositionComponent.mapper.get(currentPick)
        val positionUnit = PositionComponent.mapper.get(unit)
        positionCurrentPick.matrix4Track = positionUnit.matrix4
        positionCurrentPick.vector3Offset.set(0f, -0.75f, 0f)
        return false
    }

    private fun getEntity(node: String): Entity? {
        var entityReturn: Entity? = null
        for (entity in engine.entities) {
            if (PhysicComponent.mapper.has(entity) && MoveComponent.mapper.has(entity)) {
                val physicComponent = PhysicComponent.mapper.get(entity)
                val userData = physicComponent.physicInstance.body.userData as String
                if (userData == node) {
                    entityReturn = entity
                }
            }
        }
        return entityReturn
    }
}