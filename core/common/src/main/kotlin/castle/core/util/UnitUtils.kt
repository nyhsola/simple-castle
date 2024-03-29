package castle.core.util

import castle.core.component.UnitComponent
import com.badlogic.ashley.core.Entity

interface UnitUtils {
    companion object {
        fun findEnemies(me: UnitComponent, units: Collection<UnitComponent>): List<UnitComponent> {
            return units.filter { me.playerName != it.playerName }
        }

        fun extractUnit(collection: Collection<Entity>): List<UnitComponent> {
            return collection.filter { UnitComponent.mapper.has(it) }.map { UnitComponent.mapper.get(it) }
        }
    }
}