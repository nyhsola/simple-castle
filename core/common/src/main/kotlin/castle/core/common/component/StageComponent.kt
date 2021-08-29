package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.scenes.scene2d.Stage

class StageComponent(val stage: Stage) : Component {
    companion object {
        val mapper: ComponentMapper<StageComponent> = ComponentMapper.getFor(StageComponent::class.java)
    }
}