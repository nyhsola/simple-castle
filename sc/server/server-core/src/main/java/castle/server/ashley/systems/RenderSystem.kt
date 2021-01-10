package castle.server.ashley.systems

import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.component.ShapeComponent
import castle.server.ashley.creator.GUICreator
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3

class RenderSystem(private val camera: Camera, private val environment: Environment,
                   guiCreator: GUICreator) : IteratingSystemAdapter(Family.one(ShapeComponent::class.java, RenderComponent::class.java)
        .all(PositionComponent::class.java).get()), EntityListener {

    private val modelBatch: ModelBatch = guiCreator.createModelBatch()

    //    private val spriteBatch = guiCreator.createSpriteBatch()
    private val shapeRenderer = guiCreator.createShapeRenderer().apply {
        setAutoShapeType(true)
    }
    private val tempVector: Vector3 = Vector3()
//    private val decalBatch: DecalBatch = guiCreator.createDecalBatch(CameraGroupStrategy(camera))
//    private val spriteBatch: SpriteBatch = SpriteBatch()
//    private val bitmapFont: BitmapFont = BitmapFont()
//    private val textTransform = Matrix4().idt().rotate(0f, 1f, 0f, 90f).scl(1f)
//    private val tempMat = Matrix4()
//
//    private var decal: Decal? = null

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
//        decal = Decal.newDecal(10f, 10f, TextureRegion(Texture(Gdx.files.internal("test.png"))), true)
//        decal?.setPosition(0f, 10f, 0f)
//        decal?.width
//        decalBatch.add(decal)
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun entityAdded(entity: Entity) {
        if (RenderComponent.mapper.has(entity)) {
            val positionComponent = PositionComponent.mapper.get(entity)
            val renderComponent = RenderComponent.mapper.get(entity)
            RenderComponent.linkPosition(positionComponent, renderComponent)
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.apply {
            glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            glClearColor(0.3f, 0.3f, 0.3f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            glEnable(GL20.GL_DEPTH_TEST)
        }


        shapeRenderer.begin()
        for (i in 0 until entities.size()) {
            if (ShapeComponent.mapper.has(entities[i])) {
                val positionComponent = PositionComponent.mapper.get(entities[i])
                val shapeComponent = ShapeComponent.mapper.get(entities[i])
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled)
                positionComponent.matrix4.getTranslation(tempVector)
                shapeRenderer.color = shapeComponent.color
                shapeRenderer.rect(tempVector.x, tempVector.y, shapeComponent.width, shapeComponent.height)
            }
        }
        shapeRenderer.end()
        modelBatch.begin(camera)
        for (i in 0 until entities.size()) {
            if (RenderComponent.mapper.has(entities[i])) {
                val renderComponent = RenderComponent.mapper.get(entities[i])
                if (!renderComponent.hide) modelBatch.render(renderComponent.modelInstance, environment)
            }
        }
        modelBatch.end()
//
//        decal?.lookAt(camera.position, camera.up)
//        decalBatch.flush()
//        decalBatch.add(decal)
//
//
//        spriteBatch.projectionMatrix = tempMat.set(camera.combined).mul(textTransform)
//        spriteBatch.begin()
//        bitmapFont.draw(spriteBatch, "Text test", 10f, 10f)
//        spriteBatch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
    }
}