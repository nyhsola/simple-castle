package castle.core.behaviour

import castle.core.behaviour.component.GroundRangeComponent
import castle.core.behaviour.controller.GroundRangeUnitController
import castle.core.component.MapComponent
import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.state.StateDelta
import castle.core.util.UnitUtils
import com.badlogic.ashley.core.Entity
import org.koin.core.annotation.Single

@Single
class GroundRangeAttackBehaviour(private val controller: GroundRangeUnitController) {
    private val init = Init()
    private val main = Main()
    private val wait = Wait()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity, delta: Float) {
            controller.init(entity)
            StateComponent.mapper.get(entity).state.changeState(main)
        }
    }

    private inner class Main : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            MapComponent.mapper.get(entity).shouldSearchEntities = true
        }

        override fun update(entity: Entity, delta: Float) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val mapComponent = MapComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            val rangeComponent = GroundRangeComponent.mapper.get(entity)
            if (mapComponent.isUnitsAround) {
                val enemies = UnitUtils.findEnemies(unitComponent, mapComponent.inRadiusUnits)
                if (enemies.isNotEmpty()) {
                    rangeComponent.targetEnemy = enemies[0]
                    stateComponent.state.changeState(wait)
                }
            }
        }

        override fun exit(entity: Entity) {
            MapComponent.mapper.get(entity).shouldSearchEntities = false
        }
    }

    private inner class Wait : StateDelta<Entity> {
        override fun update(entity: Entity, delta: Float) {
            val rangeComponent = GroundRangeComponent.mapper.get(entity)
            val unitComponent = UnitComponent.mapper.get(entity)
            val stateComponent = StateComponent.mapper.get(entity)
            val targetEnemy = rangeComponent.targetEnemy!!
            rangeComponent.attackTask.update(delta)
            when {
                rangeComponent.isDone -> {
                    controller.throwProjectile(unitComponent, targetEnemy)
                    rangeComponent.isDone = false
                    rangeComponent.attackTask.reset()
                    stateComponent.state.changeState(main)
                }
                targetEnemy.isDead -> {
                    stateComponent.state.changeState(main)
                }
            }
        }
    }
}