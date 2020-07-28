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
    private val units: MutableMap<String, PlayerUnit> = HashMap()

    fun spawnOnStartPositions() {
        startPositions
                .map { startPosition -> PlayerUnit(sceneManager.getConstructor(unitType)!!, startPosition) }
                .onEach { playerUnit -> units[playerUnit.id] = playerUnit }
                .forEach { playerUnit ->
                    run {
                        sceneManager.addObjects(playerUnit)
                        physicWorld.addRigidBody(playerUnit.physicObject)
                    }
                }
    }

    fun update() {
        units.values.forEach { playerUnit -> playerUnit.update() }
    }

    fun onContactStarted(baseObject1: BaseObject, baseObject2: BaseObject) {
        if (baseObject1.id in units) {
            move(baseObject1 as PlayerUnit, baseObject2)
        }

        if (baseObject2.id in units) {
            move(baseObject2 as PlayerUnit, baseObject1)
        }
    }

    private fun move(unit: PlayerUnit, area: BaseObject) {
        if (area in path.flatten()) {
            val nextPoint = path.flatten()[path.flatten().indexOf(area) + 1]
            unit.setMovePoint(nextPoint.modelInstance.transform.getTranslation(Vector3()))
        }
    }
}