package com.simple.castle.server.kt.controller

import com.badlogic.gdx.math.Vector3
import com.simple.castle.server.kt.composition.BaseObject
import com.simple.castle.server.kt.manager.SceneManager
import com.simple.castle.server.kt.physic.PhysicWorld

class Player(private val playerName: String,
             private val unitType: String,
             private val path: List<List<BaseObject>>,
             private val startPositions: List<Vector3>,
             private val sceneManager: SceneManager,
             private val physicWorld: PhysicWorld) {
    fun spawnOnStartPositions() {
        startPositions
                .map { startPosition -> PlayerUnit(sceneManager.getConstructor(unitType), startPosition, playerName) }
                .forEach { playerUnit ->
                    run {
                        sceneManager.addObjects(playerUnit)
                        physicWorld.addRigidBody(playerUnit.physicObject)
                    }
                }
    }
}