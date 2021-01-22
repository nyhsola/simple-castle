package castle.server.ashley.service

import castle.server.ashley.creator.GUICreator
import castle.server.ashley.event.EventContext
import castle.server.ashley.game.BaseUnit
import castle.server.ashley.game.Chat
import castle.server.ashley.game.Minimap
import castle.server.ashley.game.Player
import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.math.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

class GameCreator(private val resourceManager: ResourceManager, private val guiCreator: GUICreator, private val physicService: PhysicService,
                  private val signal: Signal<EventContext>) {
    private companion object {
        const val scanStepX = 1.7f
        const val scanStepY = 1.7f
        const val scanStepZ = 1.7f
        const val occupied = 1
        const val free = 0
    }

    private val constructorMap: Map<String, Constructor> =
            resourceManager.sceneObjectsJson.map { sceneObjectJson -> Constructor(resourceManager.model, sceneObjectJson) }
                    .associateBy(keySelector = { constructor -> constructor.node })
                    .toMap()

    fun createGameEnvironment(engine: Engine) = constructorMap.filter { entry -> entry.value.instantiate }.forEach { entry -> BaseUnit(engine, entry.value) }

    fun createPlayers(): List<Player> = resourceManager.players.map { Player(it, constructorMap) }

    fun createChat(engine: Engine) = Chat(engine, guiCreator, signal, resourceManager)

    fun createMinimap(engine: Engine) = Minimap(engine, intMap)

    private val intMap: Array<IntArray> by lazy {
        val physicObject = constructorMap["ground"]?.getPhysicInstance()
        val aabbMin = Vector3()
        val aabbMax = Vector3()
        physicObject?.body?.getAabb(aabbMin, aabbMax)
        physicObject?.dispose()
        scanRegion(aabbMax, aabbMin)
    }

    // TODO: 1/23/2021 Move scan to another service
    private fun scanRegion(aabbMax: Vector3, aabbMin: Vector3): Array<IntArray> {
        val halfBox = Vector3(scanStepX, scanStepY, scanStepZ)
        val rateX = (sqrt((aabbMax.x - aabbMin.x).pow(2)) / (halfBox.x * 2)).toInt()
        val rateZ = (sqrt((aabbMax.z - aabbMin.z).pow(2)) / (halfBox.z * 2)).toInt()
        val map = Array(rateX, init = { IntArray(rateZ) })
        for (i in 1..rateX) {
            for (j in 1..rateZ) {
                val position = getPosition(i, j, halfBox, aabbMin, aabbMax)
                val hasCollisions = physicService.hasCollisions(position, halfBox)
                map[i - 1][j - 1] = if (hasCollisions) occupied else free
            }
        }
        return map
    }

    private fun getPosition(row: Int, col: Int, halfBox: Vector3, startAabbMin: Vector3, startAabbMax: Vector3): Vector3 {
        val width = halfBox.x
        val depth = halfBox.z
        return Vector3(startAabbMin.x + row * (width * 2), startAabbMax.y - (startAabbMax.y - startAabbMin.y) / 2, startAabbMax.z - col * (depth * 2))
    }
}