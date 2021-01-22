package castle.server.ashley.path

import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.ShapeComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Area(engine: Engine) {
    companion object {
        const val occupiedColor = "#10571e"
        const val freeColor = "#1ca637"
    }

    private val entity = engine.createEntity()
    private val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java)
    private val shapeComponent: ShapeComponent = engine.createComponent(ShapeComponent::class.java)
    private val offset: Vector3 = Vector3()

    val indexPosition: Vector2 = Vector2()
    var index: Int = 0

    init {
        positionComponent.matrix4 = Matrix4()
        engine.addEntity(entity.apply {
            add(positionComponent)
            add(shapeComponent)
        })
    }

    fun setPosition(paramIndex: Vector2, offsetParam: Vector3) {
        indexPosition.set(paramIndex)
        offset.set(offsetParam)
        positionComponent.matrix4.setTranslation(offset.x + indexPosition.x * shapeComponent.width, offset.y + indexPosition.y * shapeComponent.height, 0f)
    }

    fun setBox(vector2: Vector2) {
        shapeComponent.width = vector2.x
        shapeComponent.height = vector2.y
    }

    fun setOccupied(value: Int) {
        shapeComponent.color = if (value == 1) Color.valueOf(occupiedColor) else Color.valueOf(freeColor)
    }
}