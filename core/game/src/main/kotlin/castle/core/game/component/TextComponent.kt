package castle.core.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector3

class TextComponent(
    var text: String,
    val offset: Vector3
) : Component {
    companion object {
        val mapper: ComponentMapper<TextComponent> = ComponentMapper.getFor(TextComponent::class.java)
    }
}