package com.simple.castle.server.kt.physic

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody

class GroundObject(constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : KinematicObject(constructionInfo) {
    val position = Vector3()
        get() = body.worldTransform.getTranslation(field)
}