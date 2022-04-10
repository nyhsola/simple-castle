package castle.core.config

import castle.core.builder.EnvironmentBuilder
import castle.core.builder.PlayerBuilder
import castle.core.builder.TemplateBuilder
import castle.core.builder.UnitBuilder
import castle.core.event.EventQueue
import castle.core.service.*
import castle.core.system.GameManagerSystem
import castle.core.system.PhysicSystem
import castle.core.system.TextCountSystem
import castle.core.system.UnitSystem
import castle.core.system.render.*
import castle.core.ui.debug.DebugUI
import castle.core.ui.game.GameUI
import castle.core.ui.service.UIService
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GameConfig : Disposable {
    private val eventQueue = EventQueue()
    private val cameraService = CameraService()
    private val commonResources = CommonResources()
    private val gameResources = GameResources()

    private val templateBuilder = TemplateBuilder(commonResources)
    private val environmentBuilder = EnvironmentBuilder(commonResources, templateBuilder)

    private val physicService = PhysicService(cameraService)
    private val scanService = ScanService(commonResources, templateBuilder, physicService)
    private val physicSystem = PhysicSystem(physicService, eventQueue)

    private val modelBatch = ModelBatch()
    private val decalBatch = DecalBatch(CameraGroupStrategy(cameraService.currentCamera.camera))
    private val spriteBatch = SpriteBatch()
    private val shapeRender = ShapeRenderer().apply { setAutoShapeType(true) }
    private fun createStage() = Stage(ScreenViewport())

    private val stageRectRenderSystem = StageRenderSystem()
    private val animationRenderSystem = AnimationRenderSystem()
    private val modelRenderSystem = ModelRenderSystem(modelBatch, cameraService)
    private val lineRenderSystem = LineRenderSystem(shapeRender, cameraService)
    private val circleRenderSystem = CircleRenderSystem(shapeRender, cameraService)
    private val textRenderSystem = TextRenderSystem(spriteBatch, gameResources, cameraService)

    private val gameUI = GameUI(scanService, commonResources, createStage(), shapeRender, eventQueue)
    private val debugUI = DebugUI(commonResources, createStage(), eventQueue)
    private val uiService = UIService(eventQueue, gameUI, debugUI)

    private val unitBuilder = UnitBuilder(commonResources, templateBuilder)
    private val environmentInitService = EnvironmentInitService(environmentBuilder, commonResources)
    private val playerBuilder = PlayerBuilder(gameResources, environmentInitService, unitBuilder)
    private val playerService = PlayerService(gameResources, playerBuilder)
    private val rayCastService = RayCastService(physicService, cameraService)
    private val mapService = MapService(eventQueue, gameUI.minimap, scanService)
    private val selectionService = SelectionService(uiService, rayCastService)

    private val hpRenderSystem = HpRenderSystem(decalBatch)
    private val unitSystem = UnitSystem(eventQueue, environmentInitService, mapService)
    private val textCountSystem = TextCountSystem()
    private val gameManagerSystem = GameManagerSystem(environmentInitService, playerService, uiService, selectionService, cameraService, eventQueue)

    val systems =
            listOf(
                    physicSystem,
                    stageRectRenderSystem,
                    animationRenderSystem,
                    modelRenderSystem,
                    lineRenderSystem,
                    circleRenderSystem,
                    hpRenderSystem,
                    unitSystem,
                    textRenderSystem,
                    textCountSystem,
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