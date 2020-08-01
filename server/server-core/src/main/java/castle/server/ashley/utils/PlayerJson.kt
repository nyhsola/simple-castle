package castle.server.ashley.utils

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue

data class PlayerJson(var playerName: String = "",
                      var unitType: String = "",
                      var paths: MutableList<MutableList<String>> = ArrayList()) : Json.Serializable {
    override fun write(json: Json?) {
        throw AssertionError("No need to write")
    }

    override fun read(json: Json?, jsonData: JsonValue?) {
        playerName = jsonData?.get("playerName")?.asString() ?: ""
        unitType = jsonData?.get("unitType")?.asString() ?: ""
        jsonData?.get("paths")?.forEach { path ->
            val list = ArrayList<String>()
            path.asStringArray().forEach { s -> list.add(s) }
            paths.add(list)
        }
    }
}

