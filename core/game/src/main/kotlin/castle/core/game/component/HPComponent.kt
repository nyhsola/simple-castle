package castle.core.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.decals.Decal
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import java.lang.Integer.max

class HPComponent(
    val amount: Int,
    width: Float,
    aboveHead: Float,
    texture: Texture,
    matrix4Param: Matrix4
) : Component {
    private val flatMap = TextureRegion.split(texture, texture.width / 4, texture.height)
        .flatMap { it.asIterable() }
        .reversed()
        .toTypedArray()
    private val split = Array<TextureRegion>().apply { addAll(*flatMap) }
    private val quaternion: Quaternion = Quaternion(Vector3.Y, 90f)

    var currentAmount = amount
    val translation: Vector3 = Vector3(0f, aboveHead, 0f)
    val matrix4: Matrix4 = matrix4Param
    val animation: Animation<TextureRegion> = Animation<TextureRegion>(10f, split)
    val decal: Decal = Decal.newDecal(width, width / 3, TextureRegion(texture), true)

    init {
        decal.textureRegion = animation.getKeyFrame(0f)
        decal.rotation = quaternion
    }

    fun takeDamage(amountParam: Int) {
        currentAmount = max(currentAmount - amountParam, 0)
    }

    companion object {
        val mapper: ComponentMapper<HPComponent> = ComponentMapper.getFor(HPComponent::class.java)
    }
}