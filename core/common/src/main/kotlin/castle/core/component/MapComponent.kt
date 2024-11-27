package castle.core.component

import castle.core.path.Area
import castle.core.util.UnitUtils
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity

class MapComponent(val isMovable: Boolean) : Component {
    companion object {
        val mapper: ComponentMapper<MapComponent> = ComponentMapper.getFor(MapComponent::class.java)
    }

    var shouldSearchEntities = false
    val inRadiusEntities: MutableList<Entity> = ArrayList()
    var currentArea: Area = Area(0, 0)
    val fitAreas = ArrayList<Area>()

    val isUnitsAround: Boolean
        get() = inRadiusUnits.isNotEmpty()
    val inRadiusUnits: List<UnitComponent>
        get() = UnitUtils.extractUnit(inRadiusEntities)
}