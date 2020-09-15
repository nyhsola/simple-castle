package castle.server.ashley.component

import castle.server.ashley.utils.json.PlayerJson
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine

class PlayerComponent : Component {
    lateinit var unitType: String
    lateinit var paths: List<List<String>>
    var spawnRate: Float = Float.MAX_VALUE
    var accumulate: Float = 0.0f

    companion object {
        val mapper: ComponentMapper<PlayerComponent> = ComponentMapper.getFor(PlayerComponent::class.java)

        fun createComponent(engine: Engine, playerJson: PlayerJson): PlayerComponent {
            val playerComponent: PlayerComponent = engine.createComponent(PlayerComponent::class.java)
            playerComponent.unitType = playerJson.unitType
            playerComponent.paths = playerJson.paths
            playerComponent.spawnRate = playerJson.spawnRate
            return playerComponent
        }

    }
}