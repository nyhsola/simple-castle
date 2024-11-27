package castle.core.service

import castle.core.path.Area
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single
import kotlin.math.abs

@Single
class AreaService(
    private val environmentService: EnvironmentService
) {
    fun toArea(i: Int, j: Int): Area {
        return setArea(Area(0, 0), i, j)
    }

    fun setArea(outArea: Area, position: Vector3): Area {
        val width = MapScanService.scanBox.x * 2
        val depth = MapScanService.scanBox.z * 2
        val posX = abs(environmentService.mapAABBMin.x + position.x)
        val poxZ = abs(environmentService.mapAABBMin.z + position.z)
        val x = posX.div(width).toInt()
        val y = poxZ.div(depth).toInt()
        return setArea(outArea, x, y)
    }

    fun setArea(outArea: Area, i: Int, j: Int): Area {
        val x = environmentService.mapAABBMax.x - i * MapScanService.scanBox.x * 2 - MapScanService.scanBox.x
        val z = environmentService.mapAABBMax.z - j * MapScanService.scanBox.z * 2 - MapScanService.scanBox.z
        outArea.set(i, j, x, z)
        return outArea
    }
}