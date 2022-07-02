package castle.core.system.render

import castle.core.component.render.StageRenderComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import org.koin.core.annotation.Single

@Single
class StageRenderSystem : IteratingSystem(Family.all(StageRenderComponent::class.java).get()), EntityListener, KtxInputAdapter, KtxScreen {
    private val inputMultiplexer = InputMultiplexer()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val stageRenderComponent = StageRenderComponent.mapper.get(entity)
        stageRenderComponent.stage.act(deltaTime)
        stageRenderComponent.stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        for (i in 0 until entities.size()) {
            val stageRenderComponent = StageRenderComponent.mapper.get(entities[i])
            stageRenderComponent.stage.viewport.update(width, height, true)
        }
    }

    override fun entityAdded(entity: Entity) {
        val stageRenderComponent = StageRenderComponent.mapper.get(entity)
        inputMultiplexer.addProcessor(stageRenderComponent.stage)
    }

    override fun entityRemoved(entity: Entity) {
        val stageRenderComponent = StageRenderComponent.mapper.get(entity)
        inputMultiplexer.removeProcessor(stageRenderComponent.stage)
        stageRenderComponent.dispose()
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