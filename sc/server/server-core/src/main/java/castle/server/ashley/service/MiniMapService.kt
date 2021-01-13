package castle.server.ashley.service

import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.ShapeComponent
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

class MiniMapService(private val playerService: PlayerService, private val physicService: PhysicService) {
    companion object {
        const val scanStepX = 1.7f
        const val scanStepY = 1.7f
        const val scanStepZ = 1.7f

        const val miniMapWidth = 300f
        const val miniMapHeight = 300f

        const val startPositionX = 1600f
        const val startPositionY = 25f

        const val occupied = 1
        const val free = 0

        const val occupiedColor = "#10571e"
        const val freeColor = "#1ca637"
    }

    private val staticMap: Array<IntArray> by lazy {
        val physicObject = playerService.constructorMap["ground"]?.getPhysicInstance()
        val aabbMin = Vector3()
        val aabbMax = Vector3()
        physicObject?.body?.getAabb(aabbMin, aabbMax)
        physicObject?.dispose()
        scanRegion(aabbMax, aabbMin)
    }

    fun createMinimap(engine: Engine) {
        val pointWidth = miniMapWidth / staticMap.size
        val pointHeight = miniMapHeight / staticMap[0].size

        for (i in staticMap.indices) {
            for (j in staticMap[i].indices) {
                val createEntity = engine.createEntity()
                val color = if (staticMap[i][j] == occupied) Color.valueOf(occupiedColor) else Color.valueOf(freeColor)
                createEntity.add(ShapeComponent.createComponent(engine, pointWidth, pointHeight, color))
                createEntity.add(engine.createComponent(PositionComponent::class.java).apply {
                    matrix4 = Matrix4().setTranslation(Vector3(startPositionX + i * pointWidth, startPositionY + j * pointHeight, 0f))
                })
                engine.addEntity(createEntity)
            }
        }
    }

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