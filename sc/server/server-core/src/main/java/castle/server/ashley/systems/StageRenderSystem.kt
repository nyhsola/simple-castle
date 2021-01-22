package castle.server.ashley.systems

import castle.server.ashley.component.StageComponent
import castle.server.ashley.systems.adapter.IteratingInputSystemAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family

class StageRenderSystem : IteratingInputSystemAdapter(family.get()), EntityListener {
    companion object {
        private val family = Family.all(StageComponent::class.java)
    }

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val stageComponent = StageComponent.MAPPER.get(entity)
        addInputProcessor(stageComponent.stage)
    }

    override fun entityRemoved(entity: Entity) {
        val stageComponent = StageComponent.MAPPER.get(entity)
        removeInputProcessor(stageComponent.stage)
    }

    override fun render(delta: Float) {
        for (i in 0 until entities.size()) {
            val stageComponent = StageComponent.MAPPER.get(entities[i])
            stageComponent.stage.act(delta)
            stageComponent.stage.draw()
        }
    }
}