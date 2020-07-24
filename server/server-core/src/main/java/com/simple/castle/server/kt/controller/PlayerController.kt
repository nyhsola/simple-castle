package com.simple.castle.server.kt.controller

import com.badlogic.gdx.math.Vector3
import com.simple.castle.server.kt.loader.PlayerJson
import com.simple.castle.server.kt.manager.SceneManager
import com.simple.castle.server.kt.physic.PhysicWorld
import java.util.*

class PlayerController(playersJson: List<PlayerJson>,
                       private val sceneManager: SceneManager,
                       private val physicWorld: PhysicWorld) {
    private val temp: Vector3 = Vector3()
    private val players: List<Player>

    init {
        players = playersJson.map { playerJson ->
            val pathsObject = playerJson.paths
                    .map { paths -> paths.mapNotNull { path -> sceneManager.getObject(path) } }
            val startPositions = pathsObject
                    .map { list -> list.first() }
                    .filter { baseObject -> Objects.nonNull(baseObject) }
                    .mapNotNull { baseObject -> baseObject.modelInstance.transform.getTranslation(temp).cpy() }
            Player(playerJson.playerName, playerJson.unitType, pathsObject, startPositions, sceneManager, physicWorld)
        }
    }

    fun spawn() {
        players.forEach { player -> player.spawnOnStartPositions() }
    }
}