package com.simple.castle.server.kt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g3d.Model
import com.simple.castle.core.kt.asset.AssetLoader
import com.simple.castle.core.kt.render.BaseCamera
import com.simple.castle.core.kt.render.BaseEnvironment
import com.simple.castle.core.kt.render.BaseRenderer
import com.simple.castle.core.kt.screen.BaseScreen
import com.simple.castle.server.kt.loader.SceneLoader
import com.simple.castle.server.kt.manager.SceneManager
import com.simple.castle.server.kt.physic.PhysicWorld

class GameScreen(private val baseRenderer: BaseRenderer) : BaseScreen() {
    private val model: Model = AssetLoader().loadModel()
    private val baseEnvironment: BaseEnvironment = BaseEnvironment()
    private val sceneManager: SceneManager = SceneManager(SceneLoader.loadSceneObjects(model), model)
    private val baseCamera: BaseCamera
    private val physicWorld: PhysicWorld
    private var isDebug = false

    init {
        baseCamera = BaseCamera(sceneManager.getObject("ground")?.modelInstance)
        physicWorld = PhysicWorld(sceneManager.all.map { baseObject -> baseObject.physicObject })
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
        return super.keyDown(keycode)
    }

}