package castle.core.system.render

import castle.core.component.render.TextRenderComponent
import castle.core.service.CameraService
import castle.core.service.GameResources
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Align
import org.koin.core.annotation.Single

@Single
class TextRenderSystem(
    private val spriteBatch: SpriteBatch,
    gameResources: GameResources,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(TextRenderComponent::class.java).get()) {
    private val tempMat = Matrix4()
    private val bitmapFont: BitmapFont = gameResources.bitmapFont

    override fun update(deltaTime: Float) {
        spriteBatch.begin()
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        super.update(deltaTime)
        spriteBatch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textRenderComponent = TextRenderComponent.mapper.get(entity)
        val camera = cameraService.currentCamera.camera
        spriteBatch.projectionMatrix = tempMat.set(camera.combined).translate(textRenderComponent.offset).rotate(Vector3.Y, 90f)
        bitmapFont.draw(spriteBatch, textRenderComponent.text, 0f, 0f, 0f, Align.center, false)
    }
}