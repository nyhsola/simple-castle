package com.simple.castle.server.kt.loader

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.utils.Json
import com.simple.castle.server.kt.loader.json.PlayerJson
import com.simple.castle.server.kt.loader.json.SceneObjectJson

class SceneLoader(model: Model) {
    private val json = Json()
    private val nodeNames = model.nodes.map { node -> node.id }.toSet()

    private fun getValuesByPattern(pattern: String): Collection<String> {
        return nodeNames.filter { nodes -> nodes.matches(Regex(pattern)) }
    }

    fun loadSceneObjects(): List<SceneObjectJson> {
        val sceneObjectsJson = json.fromJson(List::class.java, SceneObjectJson::class.java, SceneLoader::class.java.getResource("/game-scene-objects.json").readText())

        return sceneObjectsJson
                .map { any -> any as SceneObjectJson }
                .map { sceneObjectJson -> Pair(sceneObjectJson, getValuesByPattern(sceneObjectJson.nodePattern)) }
                .map { pair -> pair.second.map { nodeName -> pair.first.copy(nodePattern = nodeName) } }
                .flatten()
    }

    fun loadPlayers(): List<PlayerJson> {
        return json.fromJson(List::class.java, PlayerJson::class.java, SceneLoader::class.java.getResource("/players.json").readText())
                .map { any -> any as PlayerJson }
    }
}