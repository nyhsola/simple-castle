package castle.core.component.render

import castle.core.ui.game.HpHud
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody

class HpRenderComponent(
    position: Matrix4,
    body: btRigidBody
) : Component {
    private val min = Vector3()
    private val max = Vector3()

    init {
        body.getAabb(min, max)
    }

    private val height = max.sub(min).y * 0.6f
    private val width = max.sub(min).z
    private val hpBarWidth = width * 22

    val translation: Vector3 = Vector3(0f, height, 0.15f)
    val matrix4: Matrix4 = position

    val someActor = HpHud.SomeActor(Texture(createPixmap())).also {
        it.setPosition(0f, 0f)
        it.setSize(hpBarWidth, 5f)
    }

    companion object {
        val mapper: ComponentMapper<HpRenderComponent> = ComponentMapper.getFor(HpRenderComponent::class.java)

        private fun createPixmap(): Pixmap {
            val width = 1
            val height = 1
            val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
            pixmap.setColor(Color.GREEN)
            pixmap.fillRectangle(0, 0, width, height)
            return pixmap
        }
    }
}