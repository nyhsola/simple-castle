package castle.core.game.service

import castle.core.common.service.ResourceService
import castle.core.game.json.PlayerJson
import castle.core.game.json.UnitJson
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

class GameResourceService : ResourceService() {
    val players: List<PlayerJson> = loadPlayers()
    val units: Map<String, UnitJson> = loadUnits().associateBy { it.unitName }
    val textures: Map<String, Texture> = loadTextures()

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

    override fun dispose() {
        textures.forEach { it.value.dispose() }
        super.dispose()
    }
}