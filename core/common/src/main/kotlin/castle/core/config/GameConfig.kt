package castle.core.config

import castle.core.behaviour.Behaviours
import castle.core.behaviour.DecorationBehaviour
import castle.core.behaviour.GroundMeleeAttackBehaviour
import castle.core.behaviour.GroundRangeAttackBehaviour
import castle.core.builder.*
import castle.core.event.EventQueue
import castle.core.service.*
import castle.core.system.GameManagerSystem
import castle.core.system.PhysicSystem
import castle.core.system.StateSystem
import castle.core.system.UnitSystem
import castle.core.system.render.*
import castle.core.ui.debug.DebugUI
import castle.core.ui.game.GameUI
import castle.core.ui.service.UIService
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GameConfig(commonConfig: CommonConfig) : Disposable {
    private val eventQueue = EventQueue()
    private val cameraService = CameraService()
    private val commonResources = commonConfig.commonResources
    private val gameResources = GameResources()

    private val modelBatch = ModelBatch()
    private val decalBatch = DecalBatch(CameraGroupStrategy(cameraService.currentCamera.camera))
    private val spriteBatch = commonConfig.spriteBatch
    private val shapeRender = ShapeRenderer().apply { setAutoShapeType(true) }
    private fun createStage() = Stage(ScreenViewport())

    private val templateBuilder = TemplateBuilder(commonResources)
    private val environmentBuilder = EnvironmentBuilder(commonResources, templateBuilder)

    private val physicService = PhysicService(cameraService)
    private val scanService = ScanService(commonResources, templateBuilder, physicService)

    private val gameUI = GameUI(scanService, commonResources, createStage(), shapeRender, eventQueue)
    private val debugUI = DebugUI(commonResources, createStage(), eventQueue)
    private val uiService = UIService(eventQueue, gameUI, debugUI)

    private val stageRectRenderSystem = StageRenderSystem()
    private val animationRenderSystem = AnimationRenderSystem()
    private val modelRenderSystem = ModelRenderSystem(modelBatch, cameraService)
    private val lineRenderSystem = LineRenderSystem(shapeRender, cameraService)
    private val circleRenderSystem = CircleRenderSystem(shapeRender, cameraService)
    private val textRenderSystem = TextRenderSystem(spriteBatch, gameResources, cameraService)

    private val environmentService = EnvironmentService(environmentBuilder)
    private val mapService = MapService(eventQueue, gameUI.minimap, scanService)
    private val unitService = UnitService(physicService, mapService, environmentService)

    private val groundMeleeAttackBehaviour = GroundMeleeAttackBehaviour(unitService)
    private val groundRangeAttackBehaviour = GroundRangeAttackBehaviour(unitService)
    private val decorationBehaviour = DecorationBehaviour(mapService)

    private val behaviours = Behaviours(groundMeleeAttackBehaviour, groundRangeAttackBehaviour, decorationBehaviour)

    private val decorationBuilder: DecorationBuilder = DecorationBuilder(commonResources, templateBuilder, behaviours)
    private val unitBuilder = UnitBuilder(commonResources, templateBuilder, behaviours)
    private val playerBuilder = PlayerBuilder(gameResources, environmentService, unitBuilder)
    private val effectBuilder = EffectBuilder(environmentService)

    private val gameService = GameService(gameResources, playerBuilder, effectBuilder, decorationBuilder)
    private val rayCastService = RayCastService(physicService, cameraService)
    private val selectionService = SelectionService(uiService, rayCastService)

    private val hpRenderSystem = HpRenderSystem(decalBatch)
    private val stateSystem = StateSystem()
    private val physicSystem = PhysicSystem(physicService, eventQueue)
    private val unitSystem = UnitSystem(eventQueue)
    private val gameManagerSystem = GameManagerSystem(environmentService, gameService, uiService, selectionService, cameraService, mapService, eventQueue)

    val systems =
        listOf(
            physicSystem,
            stageRectRenderSystem,
            animationRenderSystem,
            modelRenderSystem,
            lineRenderSystem,
            circleRenderSystem,
            hpRenderSystem,
            stateSystem,
            unitSystem,
            textRenderSystem,
            gameManagerSystem
        )

    override fun dispose() {
        commonResources.dispose()

        modelBatch.dispose()
        decalBatch.dispose()
        spriteBatch.dispose()
        shapeRender.dispose()

        gameUI.dispose()
        debugUI.dispose()
    }
}