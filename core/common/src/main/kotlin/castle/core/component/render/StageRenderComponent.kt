package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable

class StageRenderComponent(val stage: Stage) : Component, Disposable {
    companion object {
        val mapper: ComponentMapper<StageRenderComponent> = ComponentMapper.getFor(StageRenderComponent::class.java)
    }

    override fun dispose() {
        stage.dispose()
    }
}