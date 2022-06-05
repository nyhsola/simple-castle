package castle.core.config

import castle.core.behaviour.Behaviours
import castle.core.behaviour.GroundMeleeAttackBehaviour
import castle.core.behaviour.GroundRangeAttackBehaviour
import castle.core.behaviour.controller.GroundRangeUnitController
import castle.core.behaviour.controller.GroundUnitController
import castle.core.builder.*
import castle.core.service.*
import castle.core.system.*
import castle.core.system.render.*
import castle.core.ui.service.UIService
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport

class GameConfig(commonConfig: CommonConfig) : Disposable {
    private val scalingViewport = ScalingViewport(Scaling.stretch, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), OrthographicCamera())

    private val engine: PooledEngine = PooledEngine()

    private val eventQueue = commonConfig.eventQueue
    private val commonResources = commonConfig.commonResources
    private val cameraService = CameraService()
    private val physicService = PhysicService(cameraService)
    private val gameResources = GameResources()

    private val modelBatch = ModelBatch()
    private val decalBatch = DecalBatch(CameraGroupStrategy(cameraService.currentCamera.camera))
    private val spriteBatch = commonConfig.spriteBatch
    private val shapeRenderer = ShapeRenderer().apply { setAutoShapeType(true) }

    private val templateBuilder = TemplateBuilder(commonResources)
    private val environmentBuilder = EnvironmentBuilder(commonResources, templateBuilder)
    private val decorationBuilder: DecorationBuilder = DecorationBuilder(commonResources, templateBuilder)
    private val projectileBuilder: ProjectileBuilder = ProjectileBuilder(templateBuilder)

    private val environmentService = EnvironmentService(engine, environmentBuilder)
    private val scanService = ScanService(environmentService, physicService)
    private val uiService = UIService(eventQueue, engine, scanService, commonResources, shapeRenderer, spriteBatch, scalingViewport)

    private val mapService = MapService(eventQueue, uiService, scanService)
    private val rayCastService = RayCastService(physicService, cameraService)
    private val selectionService = SelectionService(engine, uiService, rayCastService)

    private val groundUnitController = GroundUnitController(mapService, environmentService)
    private val groundRangeUnitController = GroundRangeUnitController(mapService, projectileBuilder)
    private val groundMeleeAttackBehaviour = GroundMeleeAttackBehaviour(groundUnitController)
    private val groundRangeAttackBehaviour = GroundRangeAttackBehaviour(groundRangeUnitController)
    private val behaviours = Behaviours(groundMeleeAttackBehaviour, groundRangeAttackBehaviour)

    private val unitBuilder = UnitBuilder(commonResources, gameResources, environmentService, templateBuilder, behaviours)
    private val textBuilder = TextBuilder(environmentService)
    private val playerBuilder = PlayerBuilder(gameResources, unitBuilder, textBuilder, eventQueue, engine)

    private val gameService = GameService(engine, eventQueue, playerBuilder, decorationBuilder)

    private val stageRectRenderSystem = StageRenderSystem()
    private val animationRenderSystem = AnimationRenderSystem()
    private val modelRenderSystem = ModelRenderSystem(modelBatch, cameraService)
    private val lineRenderSystem = LineRenderSystem(shapeRenderer, cameraService)
    private val circleRenderSystem = CircleRenderSystem(shapeRenderer, cameraService)
    private val textRenderSystem = TextRenderSystem(spriteBatch, gameResources, cameraService)
    private val hpRenderSystem = HpRenderSystem(decalBatch)
    private val stateSystem = StateSystem()
    private val physicSystem = PhysicSystem(eventQueue, physicService)
    private val unitSystem = UnitSystem(eventQueue, physicService)
    private val mapSystem = MapSystem(mapService)
    private val gameManagerSystem = GameManagerSystem(environmentService, gameService, uiService, selectionService, cameraService, mapService, eventQueue)

    val screenConfig = ScreenConfig(
        engine,
        linkedSetOf(
            animationRenderSystem,
            modelRenderSystem,
            circleRenderSystem,
            lineRenderSystem,
            textRenderSystem,
            hpRenderSystem,
            stageRectRenderSystem,
            physicSystem,
            stateSystem,
            unitSystem,
            mapSystem,
            gameManagerSystem
        )
    )

    override fun dispose() {
        modelBatch.dispose()
        decalBatch.dispose()
        shapeRenderer.dispose()
    }
}