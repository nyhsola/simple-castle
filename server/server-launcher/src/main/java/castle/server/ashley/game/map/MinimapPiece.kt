package castle.server.ashley.game.map

import castle.server.ashley.systems.component.RectComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class MinimapPiece(
    engine: Engine, width: Float, height: Float,
    private val groundHeight: Int,
    paramIndex: Vector2, offsetParam: Vector3
) {
    private val entity = engine.createEntity()
    private val rectComponent: RectComponent = engine.createComponent(RectComponent::class.java)

    init {
        rectComponent.position = Vector2()
        engine.addEntity(entity.apply {
            add(rectComponent)
        })
        rectComponent.height = height
        rectComponent.width = width
        rectComponent.position.set(
            offsetParam.x + paramIndex.x * rectComponent.width,
            offsetParam.y + paramIndex.y * rectComponent.height
        )
        rectComponent.color = getGroundColor(groundHeight)
    }

    fun setColor(color: Color) {
        rectComponent.color = color
    }

    fun reset() {
        rectComponent.color = getGroundColor(groundHeight)
    }

    private fun getGroundColor(value: Int): Color {
        val baseHsv = FloatArray(3)
        Color.valueOf("#00ff4c").toHsv(baseHsv)
        baseHsv[2] = baseHsv[2] - 0.2f * value
        return Color().fromHsv(baseHsv)
    }
}