package castle.core.service

import castle.core.json.PlayerJson
import castle.core.json.UnitJson
import castle.core.util.LoadUtils
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import org.koin.core.annotation.Single

@Single
class GameResources {
    val players: Map<String, PlayerJson> = loadPlayers().associateBy { it.playerName }
    val units: Map<String, UnitJson> = loadUnits().associateBy { it.unitName }
    val bitmapFont: BitmapFont = loadFont()

    private fun loadPlayers(): List<PlayerJson> {
        return LoadUtils.loadList("/player.json", PlayerJson::class.java)
    }

    private fun loadUnits(): List<UnitJson> {
        return LoadUtils.loadList("/unit.json", UnitJson::class.java)
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
}