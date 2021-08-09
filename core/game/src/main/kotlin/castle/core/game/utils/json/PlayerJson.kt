package castle.core.game.utils.json

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue

data class PlayerJson(
    var unitType: String = "",
    var spawnRate: Float = 1f,
    var paths: List<List<String>> = emptyList()
) : Json.Serializable {
    override fun write(json: Json?) {
        throw AssertionError("No need to write")
    }

    override fun read(json: Json?, jsonData: JsonValue?) {
        unitType = jsonData?.get("unitType")?.asString() ?: ""
        spawnRate = jsonData?.get("spawnRate")?.asFloat() ?: 1f
        val pathsList: MutableList<MutableList<String>> = ArrayList()
        jsonData?.get("paths")?.forEach { path ->
            val list = ArrayList<String>()
            path.asStringArray().forEach { s -> list.add(s) }
            pathsList.add(list)
        }
        paths = ArrayList(pathsList)
    }
}

