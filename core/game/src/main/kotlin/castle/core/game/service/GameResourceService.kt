package castle.core.game.service

import castle.core.common.service.ResourceService
import castle.core.game.json.PlayerJson
import castle.core.game.json.UnitJson
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

class GameResourceService : ResourceService() {
    val players: List<PlayerJson> = loadPlayers()
    val units: Map<String, UnitJson> = loadUnits().associateBy { it.unitName }
    val textures: Map<String, Texture> = loadTextures()
    val bitmapFont: BitmapFont = loadFont()

    private fun loadTextures(): Map<String, Texture> {
        val map = HashMap<String, Texture>()
        val fileHandle = Gdx.files.getFileHandle("assets2d", Files.FileType.Internal)
        for (entry in fileHandle.list()) {
            map[entry.nameWithoutExtension()] = Texture(entry)
        }
        return map
    }

    private fun loadPlayers(): List<PlayerJson> {
        return loadList("/player.json", PlayerJson::class.java)
    }

    private fun loadUnits(): List<UnitJson> {
        return loadList("/unit.json", UnitJson::class.java)
    }

    private fun loadFont(): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("data/open.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 64
        parameter.magFilter = Texture.TextureFilter.Linear
        parameter.minFilter = Texture.TextureFilter.Linear
        val font32 = generator.generateFont(parameter)
        font32.data.setScale(0.05f)
        generator.dispose()
        return font32
    }

    override fun dispose() {
        textures.forEach { it.value.dispose() }
        super.dispose()
    }
}