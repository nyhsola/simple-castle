package castle.core.behaviour

import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.service.UnitService
import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity

class GroundRangeAttackBehaviour(private val unitService: UnitService) {
    private val init = Init()
    private val main = Main()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            unitService.updateMap(unitComponent)
            stateComponent.state.changeState(main)
        }
    }

    private inner class Main : StateDelta<Entity>
}