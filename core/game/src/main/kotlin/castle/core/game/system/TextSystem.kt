package castle.core.game.system

import castle.core.common.config.GUIConfig
import castle.core.common.service.CameraService
import castle.core.game.component.TextComponent
import castle.core.game.service.GameResourceService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Align

class TextSystem(guiConfig: GUIConfig, gameResourceService: GameResourceService, private val cameraService: CameraService) :
    IteratingSystem(family) {
    private val tempMat = Matrix4()
    private val textTransform = Matrix4().idt().rotate(0f, 1f, 0f, 90f)
    private val spriteBatch = guiConfig.spriteBatch
    private val bitmapFont: BitmapFont = gameResourceService.bitmapFont

    private companion object {
        private val family: Family = Family.all(TextComponent::class.java).get()
    }

    override fun update(deltaTime: Float) {
        Gdx.gl.apply { glEnable(GL20.GL_DEPTH_TEST) }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textComponent = TextComponent.mapper.get(entity)
        val camera = cameraService.currentCamera.camera
        spriteBatch.projectionMatrix = tempMat.set(camera.combined).mul(textTransform).translate(textComponent.offset)
        spriteBatch.begin()
        bitmapFont.draw(spriteBatch, textComponent.text, 0f, 0f, 0f, Align.center, false)
        spriteBatch.end()
    }
}