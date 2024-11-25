package castle.core.service

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import org.koin.core.annotation.Single
import kotlin.math.abs

@Single
class MapScanService(
    private val environmentService: EnvironmentService,
    private val physicService: PhysicService
) {
    companion object {
        val scanBox = Vector3(1.1f, 1.1f, 1.1f)
        private const val OCCUPIED = 1
        private const val FREE = 0
    }

    private val tempVector: Vector3 = Vector3()
    private val map3D: MutableList<MutableList<MutableList<Int>>> = ArrayList()
    private val map2D: MutableList<MutableList<Int>> = ArrayList()

    val map: List<List<Int>>
        get() = map2D
    val width: Int
        get() = map2D.size
    val height: Int
        get() = map2D[0].size

    fun init() {
        map3D.addAll(scanRegion(scanBox, environmentService.mapAABBMin, environmentService.mapAABBMax))
        map2D.addAll(mirror(map3D.map { byX -> byX.map { byZ -> byZ.sum() } }))
    }

    private fun scanRegion(boxScan: Vector3, aabbMin: Vector3, aabbMax: Vector3): MutableList<MutableList<MutableList<Int>>> {
        val width = boxScan.x * 2
        val depth = boxScan.z * 2
        val height = boxScan.y * 2
        val byX = (abs(aabbMax.x - aabbMin.x) / width).toInt()
        val byZ = (abs(aabbMax.z - aabbMin.z) / depth).toInt()
        val byY = (abs(aabbMax.y - aabbMin.y) / height).toInt()
        val map: MutableList<MutableList<MutableList<Int>>> = ArrayList()
        val shape = btBoxShape(boxScan)
        for (i in 0 until byX) {
            map.add(ArrayList())
            for (j in 0 until byZ) {
                map[i].add(ArrayList())
                for (k in 0 until byY) {
                    tempVector.set(aabbMax.x - i * width, aabbMax.y - k * height, aabbMax.z - j * depth)
                    val indicator = if (physicService.hasCollisions(tempVector, shape)) OCCUPIED else FREE
                    map[i][j].add(indicator)
                }
            }
        }
        shape.dispose()
        return map
    }

    private fun mirror(map2D: List<List<Int>>): MutableList<MutableList<Int>> {
        val map = map2D.map { it.toMutableList() }.toMutableList()
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (i > j && (j < map.size - 1) && (i < map[i].size - 1)) {
                    val temp = map[i][j]
                    map[i][j] = map[j][i]
                    map[j][i] = temp
                }
            }
        }
        return map
    }
}