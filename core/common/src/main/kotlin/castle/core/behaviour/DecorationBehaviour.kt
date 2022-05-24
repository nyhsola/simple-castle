package castle.core.behaviour

import castle.core.component.StateComponent
import castle.core.service.MapService
import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity

class DecorationBehaviour(private val mapService: MapService) {
    private val init = Init()
    private val main = Main()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity, delta: Float) {
            val stateComponent = StateComponent.mapper.get(entity)
            mapService.updateEntity(entity)
            stateComponent.state.changeState(main)
        }
    }

    private inner class Main : StateDelta<Entity>
}