package castle.core.game.`object`

import castle.core.common.component.PositionComponent
import castle.core.common.component.Rect3DComponent
import castle.core.game.GameContext
import castle.core.game.`object`.unit.GameObject
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class HPBar(gameObject: GameObject, private val gameContext: GameContext) : Disposable {
    private val entity: Entity = gameContext.engine.createEntity()
    private val positionComponent: PositionComponent = PositionComponent(gameObject.worldTransform)
    private val rect3DComponent: Rect3DComponent = Rect3DComponent()

    private val width: Float = 1.5f

    init {
        rect3DComponent.width = width
        rect3DComponent.height = 0.2f
        rect3DComponent.offset = Vector3(0f, 1f, rect3DComponent.width / 2)
        entity.apply {
            add(positionComponent)
            add(rect3DComponent)
        }
        gameContext.engine.addEntity(entity)
    }

    fun setPercentage(percentage: Float) {
        rect3DComponent.width = width * percentage
    }

    override fun dispose() {
        gameContext.engine.removeEntity(entity)
    }
}