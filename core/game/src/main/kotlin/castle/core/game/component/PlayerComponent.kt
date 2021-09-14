package castle.core.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper

class PlayerComponent(
    val playerName: String
) : Component {
    companion object {
        val mapper: ComponentMapper<PlayerComponent> = ComponentMapper.getFor(PlayerComponent::class.java)
    }
}