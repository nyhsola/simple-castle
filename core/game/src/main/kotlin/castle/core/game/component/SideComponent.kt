package castle.core.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper

class SideComponent(
    val side: String
) : Component {
    companion object {
        val mapper: ComponentMapper<SideComponent> = ComponentMapper.getFor(SideComponent::class.java)
    }
}