package com.simple.castle.server.kt.loader

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.model.Node
import com.badlogic.gdx.utils.Json
import com.simple.castle.server.kt.game.ServerGame
import com.simple.castle.server.kt.loader.json.SceneObjectJson
import com.simple.castle.server.kt.loader.json.SceneObjectsJson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.StreamSupport

object SceneLoader {
    private val JSON = Json()
    private fun loadData(path: String): String {
        try {
            BufferedReader(InputStreamReader(ServerGame::class.java.getResourceAsStream(path), StandardCharsets.UTF_8)).use { br -> return br.lines().collect(Collectors.joining(System.lineSeparator())) }
        } catch (exception: IOException) {
            throw AssertionError("Missing such props", exception)
        }
    }

    private fun getValuesByPattern(modelNames: Set<String>, pattern: String?): Collection<String> {
        return modelNames.stream()
                .filter { nodes: String -> nodes.matches(Regex(pattern!!)) }
                .collect(Collectors.toList())
    }

    fun loadSceneObjects(model: Model): SceneObjectsJson {
        val nodeNames = extractAllNodeNames(model)
        val sceneObjectsJson = JSON.fromJson(SceneObjectsJson::class.java, loadData("/game-scene-objects.json"))
        val extracted = sceneObjectsJson.sceneObjectsJson!!.stream()
                .map(extractImplicitNodeNames(nodeNames))
                .flatMap { obj: List<SceneObjectJson?> -> obj.stream() }
                .collect(Collectors.toList())
        sceneObjectsJson.sceneObjectsJson = extracted
        return sceneObjectsJson
    }

    private fun extractImplicitNodeNames(nodeNames: Set<String>): Function<SceneObjectJson?, List<SceneObjectJson>> {
        return Function { sceneObjectJson: SceneObjectJson? ->
            val nodes = getValuesByPattern(nodeNames, sceneObjectJson!!.nodePattern)
            nodes.stream().map { implicitNode: String? ->
                val sceneObjectJsonNew = SceneObjectJson(sceneObjectJson)
                sceneObjectJsonNew.nodePattern = implicitNode
                sceneObjectJsonNew
            }.collect(Collectors.toList())
        }
    }

    private fun extractAllNodeNames(model: Model): Set<String> {
        return StreamSupport.stream(model.nodes.spliterator(), false)
                .map { node: Node -> node.id }
                .collect(Collectors.toSet())
    }
}