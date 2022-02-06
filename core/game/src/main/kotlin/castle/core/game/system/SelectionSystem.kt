package castle.core.game.system

import castle.core.common.component.PhysicComponent
import castle.core.game.component.SideComponent
import castle.core.game.service.RayCastService
import castle.core.game.service.SelectionService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import ktx.app.KtxInputAdapter

class SelectionSystem(
    private val rayCastService: RayCastService,
    private val selectionService: SelectionService
) : EntitySystem(), KtxInputAdapter {

    override fun addedToEngine(engine: Engine) {
        engine.addEntity(selectionService.circle)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val userPick = getEntity((rayCastService.rayCast(screenX.toFloat(), screenY.toFloat())?.userData ?: "") as String)
        if (userPick == null) {
            selectionService.unSelect()
        } else {
            selectionService.select(userPick)
        }
        return false
    }

    private fun getEntity(node: String): Entity? {
        if (node == "") {
            return null
        }
        var entityReturn: Entity? = null
        for (entity in engine.entities) {
            if (PhysicComponent.mapper.has(entity) && SideComponent.mapper.has(entity)) {
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