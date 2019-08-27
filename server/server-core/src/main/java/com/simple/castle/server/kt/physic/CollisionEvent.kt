package com.simple.castle.server.kt.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject

interface CollisionEvent {
    fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject)
}