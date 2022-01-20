package castle.core.game.component

import castle.core.game.json.PlayerJson
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper

class PlayerComponent(
    val playerJson: PlayerJson
) : Component {
    companion object {
        val mapper: ComponentMapper<PlayerComponent> = ComponentMapper.getFor(PlayerComponent::class.java)
    }
}