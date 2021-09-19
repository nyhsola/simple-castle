package castle.core.common.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable

class StageComponent(val stage: Stage) : Component, Disposable {
    companion object {
        val mapper: ComponentMapper<StageComponent> = ComponentMapper.getFor(StageComponent::class.java)
    }

    override fun dispose() {
        stage.dispose()
    }
}