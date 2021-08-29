package castle.core.game

import castle.core.common.config.GUIConfig
import castle.core.common.service.CameraService
import castle.core.game.`object`.GameEnvironment
import castle.core.game.`object`.Players
import castle.core.game.event.EventQueue
import castle.core.game.event.EventType
import castle.core.game.service.RayCastService
import castle.core.game.service.ScanService
import castle.core.common.service.ResourceService
import castle.core.common.service.PhysicService
import castle.core.game.`object`.ui.GameUI
import castle.core.game.service.MapService
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

class GameManager(
    engine: Engine,
    guiConfig: GUIConfig,
    private val physicService: PhysicService,
    private val cameraService: CameraService
) : KtxInputAdapter, Disposable {
    private val resourceService: ResourceService = ResourceService()
    private val gameContext: GameContext = GameContext(engine, resourceService)
    private val gameEnvironment = GameEnvironment(gameContext)
    private val eventQueue = EventQueue()

    private val rayCastService = RayCastService(physicService, cameraService)
    private val scanService = ScanService(gameContext, physicService)
    private val mapService = MapService(scanService)

    private val gameUI = GameUI(gameContext, scanService, guiConfig, eventQueue)
    private val players: Players = Players(gameContext, mapService)

    private val temp = Vector3()

    fun update(delta: Float) {
        proceedEvents()
        mapService.update(players)
        gameUI.minimap.update(mapService.objectsOnMap)
        players.update(delta)
        proceedMessages()
        dispatchAiMessages(delta)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val collisionObject = rayCastService.rayCast(screenX.toFloat(), screenY.toFloat())
        if (collisionObject != null) {
            collisionObject.worldTransform.getTranslation(temp)

            val nodeStr = collisionObject.userData as String
            val position = "X: ${temp.x}\nY:${temp.y}\nZ: ${temp.z}"

            gameUI.chat.typeMessage("$nodeStr\n$position")
        }
        return super.touchDown(screenX, screenY, pointer, button)
    }

    private fun proceedEvents() {
        val gameEvents = eventQueue.pollAll()
        for (gameEvent in gameEvents) {
            when (gameEvent.eventType) {
                EventType.CHAT_FOCUSED -> {
                    cameraService.input = false
                }
                EventType.CHAT_UNFOCUSED -> {
                    cameraService.input = true
                }
            }
        }
    }

    private fun proceedMessages() {
        val messages = gameUI.chat.pollAllMessages()
        for (message in messages) {
            if (message.contains("debug-physic")) {
                physicService.debugEnabled = !physicService.debugEnabled
            }
            if (message.contains("debug-ui")) {
                gameUI.debugEnabled = !gameUI.debugEnabled
            }
            if (message.contains("spawn")) {
                players.spawn()
            }
        }
    }

    private fun dispatchAiMessages(delta: Float) {
        GdxAI.getTimepiece().update(delta)
        MessageManager.getInstance().update()
    }

    override fun dispose() {
        resourceService.dispose()
        gameEnvironment.dispose()
        gameUI.dispose()
        players.dispose()
    }
}