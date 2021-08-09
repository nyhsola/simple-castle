package castle.core.game.service

import castle.core.physic.service.PhysicService
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import kotlin.math.abs

class ScanService(private val physicService: PhysicService) {
    companion object {
        const val occupied = 1
        const val free = 0
    }

    fun scanRegion(boxScan: Vector3, aabbMin: Vector3, aabbMax: Vector3): List<List<List<Int>>> {
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
                    val position = Vector3(aabbMax.x - i * width, aabbMax.y - k * height, aabbMax.z - j * depth)
                    val hasCollisions = physicService.hasCollisions(position, shape)
                    map[i][j].add(if (hasCollisions) occupied else free)
                }
            }
        }
        shape.dispose()
        return map
    }
}