package castle.core.game.service

import castle.core.common.service.PhysicService
import castle.core.common.component.PhysicComponent
import castle.core.game.path.Area
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import kotlin.math.abs

class ScanService(
    gameResourceService: GameResourceService,
    private val physicService: PhysicService
) {
    companion object {
        private const val occupied = 1
        private const val free = 0

        val SCAN_BOX = Vector3(1.7f, 1.7f, 1.7f)

        val aabbMin = Vector3()
        val aabbMax = Vector3()
    }

    private val tempVector: Vector3 = Vector3()
    private val map3D by lazy { initMap3D(gameResourceService) }
    val map by lazy { initMap2D() }

    fun toArea(position: Vector3): Area {
        val width = SCAN_BOX.x * 2
        val depth = SCAN_BOX.z * 2
        val x = (abs(aabbMin.x + position.x) / width).toInt()
        val y = (abs(aabbMin.z + position.z) / depth).toInt()
        return Area(x, y)
    }

    private fun initMap3D(gameResourceService: GameResourceService): List<List<List<Int>>> {
        return gameResourceService.templates.getValue("GROUND").buildUnit(gameResourceService.model, "ground")
            .apply { getComponent(PhysicComponent::class.java).physicInstance.body.getAabb(aabbMin, aabbMax) }
            .apply { dispose() }
            .let { scanRegion(SCAN_BOX, aabbMin, aabbMax) }
    }

    private fun initMap2D() = mirror(map3D.map { byX -> byX.map { byZ -> byZ.sum() } })

    private fun scanRegion(boxScan: Vector3, aabbMin: Vector3, aabbMax: Vector3): List<List<List<Int>>> {
        val width = boxScan.x * 2
        val depth = boxScan.z * 2
        val height = boxScan.y * 2
        val byX = (abs(aabbMax.x - aabbMin.x) / width).toInt()
        val byZ = (abs(aabbMax.z - aabbMin.z) / depth).toInt()
        val byY = (abs(aabbMax.y - aabbMin.y) / height).toInt()
        val map = ArrayList<ArrayList<ArrayList<Int>>>()
        val shape = btBoxShape(boxScan)
        for (i in 0 until byX) {
            map.add(ArrayList())
            for (j in 0 until byZ) {
                map[i].add(ArrayList())
                for (k in 0 until byY) {
                    tempVector.set(aabbMax.x - i * width, aabbMax.y - k * height, aabbMax.z - j * depth)
                    val indicator = if (physicService.hasCollisions(tempVector, shape)) occupied else free
                    map[i][j].add(indicator)
                }
            }
        }
        shape.dispose()
        return map
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
}