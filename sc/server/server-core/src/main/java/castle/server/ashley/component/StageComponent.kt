package castle.server.ashley.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.scenes.scene2d.Stage

class StageComponent : Component {
    lateinit var stage: Stage

    companion object {
        val MAPPER: ComponentMapper<StageComponent> = ComponentMapper.getFor(StageComponent::class.java)
    }
}