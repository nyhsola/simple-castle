package com.simple.castle.server.kt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.Vector3
import com.simple.castle.core.asset.AssetLoader
import com.simple.castle.core.render.BaseCamera
import com.simple.castle.core.render.BaseEnvironment
import com.simple.castle.core.render.BaseRenderer
import com.simple.castle.core.screen.BaseScreen
import com.simple.castle.server.composition.BaseObject
import com.simple.castle.server.loader.SceneLoader
import com.simple.castle.server.manager.SceneManager
import com.simple.castle.server.physic.world.PhysicWorld
import java.util.function.Consumer

class GameScreen(private val baseRenderer: BaseRenderer) : BaseScreen() {
    private val model: Model = AssetLoader().loadModel()
    private val baseEnvironment: BaseEnvironment = BaseEnvironment()
    private val sceneManager: SceneManager
    private val baseCamera: BaseCamera
    private val physicWorld: PhysicWorld
    private var isDebug = false

    init {
        sceneManager = SceneManager(SceneLoader.loadSceneObjects(model), model)
        val groundObject = sceneManager.getObject("ground").modelInstance
        val ground = groundObject.transform.getTranslation(Vector3())
        baseCamera = BaseCamera(ground, groundObject)
        physicWorld = PhysicWorld()
        sceneManager.all.forEach(Consumer { baseObject: BaseObject -> physicWorld.addRigidBody(baseObject.physicObject) })
        inputMultiplexer.addProcessor(baseCamera)
    }

    override fun render(delta: Float) {
        baseCamera.update(delta)
        physicWorld.update(delta)
        baseRenderer.render(baseCamera, sceneManager.drawables, baseEnvironment)
        if (isDebug) {
            physicWorld.debugDraw(baseCamera)
        }
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