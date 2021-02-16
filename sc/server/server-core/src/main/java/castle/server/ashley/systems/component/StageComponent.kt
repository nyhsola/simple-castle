package castle.server.ashley.systems.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.scenes.scene2d.Stage

class StageComponent : Component {
    lateinit var stage: Stage

    companion object {
        val mapper: ComponentMapper<StageComponent> = ComponentMapper.getFor(StageComponent::class.java)
    }
}