package castle.server.ashley.systems

import castle.server.ashley.component.PositionComponent
import castle.server.ashley.screen.ConstructorManager
import castle.server.ashley.systems.adapter.IntervalSystemAdapter

class UnitSpawnerSystem(interval: Float, private val constructorManager: ConstructorManager) : IntervalSystemAdapter(interval) {

    override fun updateInterval() {
        val instantiate = constructorManager.constructorMap["n-unit-type-model"]?.instantiate(engine)

        val moveTo = constructorManager.constructorMap["sp-1-1-1"]?.getTransform()
        if (moveTo != null) PositionComponent.mapper.get(instantiate).matrix4 = moveTo

        engine.addEntity(instantiate)
    }
}