package castle.core.game.`object`

import castle.core.common.component.RectComponent
import castle.core.game.`object`.unit.GameObject
import castle.core.game.path.Area
import castle.core.game.path.AreaGraph
import castle.core.game.service.ScanService
import castle.core.game.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.math.abs

class GameMap(
    private val engine: Engine,
    screenWidth: Int, screenHeight: Int,
    scanService: ScanService,
    resourceManager: ResourceManager
) {
    companion object {
        private val SCAN_BOX = Vector3(1.7f, 1.7f, 1.7f)
    }

    private val miniMapWidth: Float = screenWidth * 0.2f
    private val miniMapHeight: Float = screenHeight * 0.3f
    private val startPositionX = screenWidth - miniMapWidth - 10f
    private val startPositionY = 10f
    private val aabbMin = Vector3()
    private val aabbMax = Vector3()
    private val map3D = resourceManager.constructorMap["ground"]!!.getPhysicInstance()
        .apply { body.getAabb(aabbMin, aabbMax) }
        .also { it.dispose() }
        .let { scanService.scanRegion(SCAN_BOX, aabbMin, aabbMax) }
    private val map2D = mirror(map3D.map { byX -> byX.map { byZ -> byZ.sum() } })
    private val mapGraph = initializeGraph(map2D)
    private val miniMap: MutableList<MutableList<MinimapPiece>> = initializeMinimap(map2D)
    private val miniMapBuffer: MutableList<MinimapPiece> = ArrayList()
    private val objMap: MutableMap<Area, GameObject> = HashMap()

    fun getPath(list: List<Vector3>) : GraphPath<Area> {
        val areas = list.map { toArea(it) }
        val path = DefaultGraphPath<Area>()
        for (i in 0 until areas.size - 2) {
            mapGraph.findPath(areas[i], areas[i + 1]).forEach { path.add(it) }
        }
        return path
    }

    fun isInRangeOfArea(position: Vector3, area: Area) = toArea(position).isInRange(area)

    fun getNearObjects(position: Vector3): List<GameObject> {
        val toArea = toArea(position)
        val areasInRange = toArea.getAreasInRange()
        return areasInRange.mapNotNull { objMap[it] }
    }

    fun update(gameObjects: List<GameObject>) {
        reset()
        gameObjects.forEach { set(it) }
    }

    private fun toArea(position: Vector3): Area {
        val width = SCAN_BOX.x * 2
        val depth = SCAN_BOX.z * 2
        val x = (abs(aabbMin.x + position.x) / width).toInt()
        val y = (abs(aabbMin.z + position.z) / depth).toInt()
        return Area(x, y)
    }

    private fun reset() {
        miniMapBuffer.forEach { it.reset() }
        miniMapBuffer.clear()
        objMap.clear()
    }

    private fun set(gameObject: GameObject) {
        val toArea = toArea(gameObject.unitPosition)
        if (toArea.x < 0 || toArea.y < 0) {
            return
        }
        objMap[toArea] = gameObject
        miniMapBuffer.add(miniMap[toArea.y][toArea.x])
        miniMap[toArea.y][toArea.x].setColor(Color.RED)
    }

    private fun initializeGraph(map2D: List<List<Int>>): AreaGraph {
        val areaGraph = AreaGraph()
        for (i in map2D.indices) {
            for (j in map2D[i].indices) {
                val position = Vector2(aabbMax.x - i * SCAN_BOX.x * 2, aabbMax.z - j * SCAN_BOX.z * 2)
                areaGraph.addArea(Area(position, i, j))
            }
        }
        map2D.forEachIndexed { i, it1 ->
            it1.forEachIndexed { j, it2 ->
                if (it2 == 0) {
                    val area = Area(i, j)
                    if (i - 1 >= 0 && map2D[i - 1][j] == 0) {
                        areaGraph.connect(area, Area(i - 1, j))
                    }
                    if (i + 1 < map2D.size && map2D[i + 1][j] == 0) {
                        areaGraph.connect(area, Area(i + 1, j))
                    }
                    if (j - 1 >= 0 && map2D[i][j - 1] == 0) {
                        areaGraph.connect(area, Area(i, j - 1))
                    }
                    if (j + 1 < map2D[i].size && map2D[i][j + 1] == 0) {
                        areaGraph.connect(area, Area(i, j + 1))
                    }
                }
            }
        }
        return areaGraph
    }

    private fun initializeMinimap(miniMap: List<List<Int>>): MutableList<MutableList<MinimapPiece>> {
        val pointWidth = miniMapWidth / miniMap.size
        val pointHeight = miniMapHeight / miniMap[0].size

        val pieces: MutableList<MutableList<MinimapPiece>> = ArrayList()
        for (i in miniMap.indices) {
            pieces.add(ArrayList())
            for (j in miniMap[i].indices) {
                pieces[i].add(
                    MinimapPiece(
                        engine,
                        pointWidth, pointHeight,
                        Vector2(i.toFloat(), j.toFloat()),
                        Vector2(startPositionX, startPositionY), miniMap[i][j]
                    )
                )
            }
        }
        return pieces
    }


    private fun mirror(map2D: List<List<Int>>): List<List<Int>> {
        val map = map2D.map { it.toMutableList() }.toMutableList()
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (i > j) {
                    val temp = map[i][j]
                    map[i][j] = map[j][i]
                    map[j][i] = temp
                }
            }
        }
        return map
    }

    private class MinimapPiece(
        engine: Engine,
        width: Float, height: Float,
        paramIndex: Vector2, offsetParam: Vector2,
        private val groundHeight: Int
    ) {
        private val entity = engine.createEntity().apply { engine.addEntity(this) }
        private val rectComponent: RectComponent =
            engine.createComponent(RectComponent::class.java).apply { entity.add(this) }

        init {
            rectComponent.height = height
            rectComponent.width = width
            rectComponent.position.set(
                offsetParam.x + paramIndex.x * rectComponent.width,
                offsetParam.y + paramIndex.y * rectComponent.height
            )
            rectComponent.color = getGroundColor(groundHeight)
        }

        fun setColor(color: Color) = color.also { rectComponent.color = it }

        fun reset() = getGroundColor(groundHeight).also { rectComponent.color = it }

        private fun getGroundColor(value: Int): Color {
            val baseHsv = FloatArray(3)
            Color.valueOf("#00ff4c").toHsv(baseHsv)
            baseHsv[2] = baseHsv[2] - 0.2f * value
            return Color().fromHsv(baseHsv)
        }
    }
}