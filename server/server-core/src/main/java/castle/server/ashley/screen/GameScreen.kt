package castle.server.ashley.screen

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.systems.CameraControlSystem
import castle.server.ashley.systems.PhysicSystem
import castle.server.ashley.systems.RenderSystem
import castle.server.ashley.utils.AssetLoader
import castle.server.ashley.utils.SceneLoader
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight


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
    }
    private val environment: Environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    init {
        camera.apply {
//            position.set(groundModel.transform.getTranslation(Vector3()).add(10f, 10f, 0f))
//            lookAt(groundModel.transform.getTranslation(Vector3()))
        }

        customEngine.apply {
            addSystem(CameraControlSystem(camera))
            addSystem(RenderSystem(camera, environment, modelBatch))
            addSystem(PhysicSystem(camera))
        }

        addInitObjects()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit()
        }
        return super.keyDown(keycode)
    }

    override fun dispose() {
        customEngine.removeAllEntities()
        model.dispose()
        super.dispose()
    }

    private fun addInitObjects() {
        constructorManager.constructorMap
                .asIterable()
                .filter { entry -> entry.value.instantiate }
                .forEach { entry ->
                    run {
                        val entity: Entity = customEngine.createEntity();

                        val renderComponent: RenderComponent = customEngine.createComponent(RenderComponent::class.java)
                        renderComponent.modelInstance = entry.value.buildModel()
                        renderComponent.hide = entry.value.hide
                        entity.add(renderComponent)

                        val physicComponent: PhysicComponent = customEngine.createComponent(PhysicComponent::class.java)
                        physicComponent.physicObject = entry.value.buildPhysic()
                        physicComponent.mass = entry.value.mass
                        entity.add(physicComponent)

                        customEngine.addEntity(entity)
                    }
                }
    }
}
