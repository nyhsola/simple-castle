package castle.core.component.render

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.decals.Decal
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array

class HPRenderComponent(
        texture: Texture,
        position: Matrix4,
        body: btRigidBody
) : Component {
    private val min = Vector3()
    private val max = Vector3()

    init {
        body.getAabb(min, max)
    }

    private val height = max.sub(min).y * 0.9f
    private val width = max.sub(min).z * 1.1f
    private val hpBarWidth = (width / 4) * 0.7f
    private val flatMap = TextureRegion.split(texture, texture.width / 4, texture.height / 4)
            .flatMap { it.asIterable() }
            .reversed()
            .toTypedArray()
    private val split = Array<TextureRegion>().apply { addAll(*flatMap) }
    private val quaternion: Quaternion = Quaternion(Vector3.Y, 90f)

    val translation: Vector3 = Vector3(0f, height, 0f)
    val matrix4: Matrix4 = position
    val animation: Animation<TextureRegion> = Animation<TextureRegion>(1000f, split)
    val decal: Decal = Decal.newDecal(width, hpBarWidth, TextureRegion(texture), true)
            .apply {
                textureRegion = animation.getKeyFrame(0f)
                rotation = quaternion
            }

    companion object {
        val mapper: ComponentMapper<HPRenderComponent> = ComponentMapper.getFor(HPRenderComponent::class.java)
    }
}