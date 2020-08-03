package castle.server.ashley.screen

import castle.server.ashley.systems.CameraControlSystem
import castle.server.ashley.systems.InitSystem
import castle.server.ashley.systems.PhysicSystem
import castle.server.ashley.systems.RenderSystem
import castle.server.ashley.utils.AssetLoader
import castle.server.ashley.utils.SceneLoader
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector3


class GameScreen(modelBatch: ModelBatch) : InputScreenAdapter() {
    private val model: Model = AssetLoader().loadModel()
    private val sceneLoader: SceneLoader = SceneLoader(model)
    private val constructorManager: ConstructorManager = ConstructorManager(sceneLoader.loadSceneObjects(), model)
    private val camera: Camera = PerspectiveCamera().apply {
        near = 1f
        far = 300f
        fieldOfView = 67f
        viewportWidth = Gdx.graphics.width.toFloat()
        viewportHeight = Gdx.graphics.height.toFloat()
        position.set(Vector3(10f, 10f, 0f))
        lookAt(Vector3.Zero)
    }
    private val environment: Environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    init {
        customEngine.apply {
            addSystem(CameraControlSystem(camera))
            addSystem(RenderSystem(camera, environment, modelBatch))
            addSystem(PhysicSystem(camera))
            addSystem(InitSystem(constructorManager))
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit()
        }
        return super.keyDown(keycode)
    }

    override fun dispose() {
        model.dispose()
        super.dispose()
    }

}
