package castle.core.behaviour

import castle.core.behaviour.controller.GroundRangeUnitController
import castle.core.component.MapComponent
import castle.core.component.StateComponent
import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity

class GroundRangeAttackBehaviour(private val controller: GroundRangeUnitController){
    private val init = Init()
    private val main = Main()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity) {
            StateComponent.mapper.get(entity).state.changeState(main)
        }
    }

    private inner class Main : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            MapComponent.mapper.get(entity).shouldSearchEntities = true
        }

        override fun update(entity: Entity) {
        }

        override fun exit(entity: Entity) {
            MapComponent.mapper.get(entity).shouldSearchEntities = false
        }
    }
}