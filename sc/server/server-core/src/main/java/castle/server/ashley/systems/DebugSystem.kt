package castle.server.ashley.systems

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import castle.server.ashley.utils.IntersectUtils
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport

class DebugSystem(
    private val resourceManager: ResourceManager,
    private val camera: Camera
) : IteratingSystemAdapter(
    Family.all(
        PositionComponent::class.java,
        RenderComponent::class.java,
        PhysicComponent::class.java
    ).get()
) {

    private val tempBoundingBox = BoundingBox()

    private var stage: Stage
    private var skin: Skin = resourceManager.skin
    private var timeButton: TextButton

    init {
        val timeLabel = Label("Info: ", skin)
        timeButton = TextButton("", skin)

        val table = Table()
        table.align(Align.topLeft)
        table.add(timeLabel, timeButton).row()

        table.setFillParent(true)

        stage = Stage(ScreenViewport())
        stage.addActor(table)
    }

    override fun render(delta: Float) {
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
        skin.dispose()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val entity = IntersectUtils.intersect(camera, entities, screenY.toFloat(), screenX.toFloat(), tempBoundingBox)

        if (entity != null) {
            val positionComponent = PositionComponent.mapper.get(entity)
            val renderComponent = RenderComponent.mapper.get(entity)
            val physicComponent = PhysicComponent.mapper.get(entity)

            val position = positionComponent.matrix4
            val nodePosition = renderComponent.modelInstance.getNode(positionComponent.nodeName).globalTransform
            val matrix4 = Matrix4()
            physicComponent.physicObject.body.getWorldTransform(matrix4)

            val text = "${positionComponent.nodeName} " +
                    System.lineSeparator() +
                    "Position: ${position.getTranslation(Vector3())} ${position.getRotation(Quaternion())} " +
                    System.lineSeparator() +
                    "Node position: ${nodePosition.getTranslation(Vector3())} ${nodePosition.getRotation(Quaternion())}"

            timeButton.setText(text)
        } else {
            timeButton.setText("")
        }

        return super.touchUp(screenX, screenY, pointer, button)
    }
}