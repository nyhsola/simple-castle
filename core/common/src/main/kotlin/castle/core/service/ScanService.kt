package castle.core.service

import castle.core.builder.TemplateBuilder
import castle.core.component.PhysicComponent
import castle.core.path.Area
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import kotlin.math.abs

class ScanService(
        commonResources: CommonResources,
        private val templateBuilder: TemplateBuilder,
        private val physicService: PhysicService
) {
    companion object {
        private const val occupied = 1
        private const val free = 0
    }

    private val tempVector: Vector3 = Vector3()
    private val map3D by lazy { initMap3D(commonResources) }
    val map by lazy { initMap2D() }

    private val scanBox = Vector3(1f, 1f, 1f)
    private val aabbMin = Vector3()
    private val aabbMax = Vector3()

    fun toArea(position: Vector3): Area {
        val width = scanBox.x * 2
        val depth = scanBox.z * 2
        val posX = abs(aabbMin.x + position.x)
        val poxZ = abs(aabbMin.z + position.z)
        val x = posX.div(width).toInt()
        val y = poxZ.div(depth).toInt()
        return toArea(x, y)
    }

    fun toArea(i: Int, j: Int): Area {
        val x = aabbMax.x - i * scanBox.x * 2 - (scanBox.x)
        val z = aabbMax.z - j * scanBox.z * 2 - (scanBox.z)
        val position = Vector2(x, z)
        return Area(position, i, j)
    }

    private fun initMap3D(commonResources: CommonResources): List<List<List<Int>>> {
        return templateBuilder.build(commonResources.templates.getValue("GROUND"), "ground")
                .apply { PhysicComponent.mapper.get(this).body.getAabb(aabbMin, aabbMax) }
                .apply { dispose() }
                .let { scanRegion(scanBox, aabbMin, aabbMax) }
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