package com.simple.castle.server.kt.loader

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.utils.Json
import com.simple.castle.server.kt.loader.json.SceneObjectJson
import com.simple.castle.server.kt.loader.json.SceneObjectsJson

class SceneLoader(model: Model) {
    private val json = Json()
    private val nodeNames = model.nodes.map { node -> node.id }.toSet()

    private fun getValuesByPattern(modelNames: Set<String>, pattern: String): Collection<String> {
        return modelNames.filter { nodes -> nodes.matches(Regex(pattern)) }
    }

    fun loadSceneObjects(): SceneObjectsJson {
        val sceneObjectsJson = json.fromJson(SceneObjectsJson::class.java, SceneLoader::class.java.getResource("/game-scene-objects.json").readText())

        val extracted = sceneObjectsJson.sceneObjectsJson
                ?.map { sceneObjectJson ->
                    getValuesByPattern(nodeNames, sceneObjectJson!!.nodePattern!!)
                            .map { implicitNode: String ->
                                SceneObjectJson(sceneObjectJson).apply { nodePattern = implicitNode }
                            }
                }
                ?.flatten()

        val sceneObjects = SceneObjectsJson()
        sceneObjects.sceneObjectsJson = extracted

        return sceneObjects
    }

}