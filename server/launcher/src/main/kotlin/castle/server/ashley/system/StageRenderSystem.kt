package castle.server.ashley.system

import castle.server.ashley.system.component.StageComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxInputAdapter

class StageRenderSystem : IteratingSystem(family.get()), EntityListener, KtxInputAdapter {
    companion object {
        private val family = Family.all(StageComponent::class.java)
    }

    private val inputMultiplexer = InputMultiplexer()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
    }

    override fun entityAdded(entity: Entity) {
        val stageComponent = StageComponent.mapper.get(entity)
        inputMultiplexer.addProcessor(stageComponent.stage)
    }

    override fun entityRemoved(entity: Entity) {
        val stageComponent = StageComponent.mapper.get(entity)
        inputMultiplexer.removeProcessor(stageComponent.stage)
    }

    override fun update(deltaTime: Float) {
        for (i in 0 until entities.size()) {
            val stageComponent = StageComponent.mapper.get(entities[i])
            stageComponent.stage.act(deltaTime)
            stageComponent.stage.draw()
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        return inputMultiplexer.keyDown(keycode)
    }

    override fun keyTyped(character: Char): Boolean {
        return inputMultiplexer.keyTyped(character)
    }

    override fun keyUp(keycode: Int): Boolean {
        return inputMultiplexer.keyUp(keycode)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return inputMultiplexer.mouseMoved(screenX, screenY)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return inputMultiplexer.scrolled(amountX, amountY)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return inputMultiplexer.touchDragged(screenX, screenY, pointer)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchUp(screenX, screenY, pointer, button)
    }
}