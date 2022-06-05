package castle.core.component

import castle.core.util.UnitUtils
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity

class MapComponent(
    val isStatic: Boolean
) : Component {
    companion object {
        val mapper: ComponentMapper<MapComponent> = ComponentMapper.getFor(MapComponent::class.java)
    }

    var once = false
    var shouldSearchEntities = false

    val inRadiusEntities: MutableList<Entity> = ArrayList()

    val isEnemiesAround: Boolean
        get() = inRadiusEnemies.isNotEmpty()
    val inRadiusEnemies: List<UnitComponent>
        get() = UnitUtils.extractUnit(inRadiusEntities)
}