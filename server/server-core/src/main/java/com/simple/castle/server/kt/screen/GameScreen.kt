package com.simple.castle.server.kt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.simple.castle.core.kt.asset.AssetLoader
import com.simple.castle.core.kt.render.BaseCamera
import com.simple.castle.core.kt.render.BaseEnvironment
import com.simple.castle.core.kt.render.BaseRenderer
import com.simple.castle.core.kt.screen.BaseScreen
import com.simple.castle.server.kt.controller.PlayerController
import com.simple.castle.server.kt.loader.SceneLoader
import com.simple.castle.server.kt.manager.SceneManager
import com.simple.castle.server.kt.physic.CollisionEvent
import com.simple.castle.server.kt.physic.PhysicWorld

class GameScreen(private val baseRenderer: BaseRenderer) : BaseScreen(), CollisionEvent {
    private val model: Model = AssetLoader().loadModel()
    private val baseEnvironment: BaseEnvironment = BaseEnvironment()
    private val sceneLoader: SceneLoader = SceneLoader(model)
    private val sceneManager: SceneManager = SceneManager(sceneLoader.loadSceneObjects(), model)
    private val physicWorld: PhysicWorld = PhysicWorld(this as CollisionEvent, sceneManager.all.map { baseObject -> baseObject.physicObject })
    private val baseCamera: BaseCamera = BaseCamera(sceneManager.getObject("ground")?.modelInstance)
    private val playerController: PlayerController = PlayerController(sceneLoader.loadPlayers(), sceneManager, physicWorld)
    private var isDebug = false

    init {
        inputMultiplexer.addProcessor(baseCamera)
    }

    override fun render(delta: Float) {
        baseCamera.update(delta)
        physicWorld.update(delta)
        baseRenderer.render(baseCamera, sceneManager.drawables, baseEnvironment)
        if (isDebug) physicWorld.debugDraw(baseCamera)
    }

    override fun resize(width: Int, height: Int) {
        baseCamera.resize(width, height)
        baseCamera.update()
    }

    override fun dispose() {
        baseRenderer.dispose()
        physicWorld.dispose()
        sceneManager.dispose()
        model.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit()
        }
        if (Input.Keys.F1 == keycode) {
            isDebug = !isDebug
        }
        if (Input.Keys.SPACE == keycode) {
            playerController.spawn()
        }
        return super.keyDown(keycode)
    }

    override fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject) {
        playerController.onContactStarted(colObj0, colObj1)
    }

}