package castle.server.ashley.service

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.ShapeComponent
import castle.server.ashley.physic.PhysicObject
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import kotlin.math.pow
import kotlin.math.sqrt

class MapService(private val playerService: PlayerService, private val physicService: PhysicService) {
    companion object {
        const val scanStepX = 1f
        const val scanStepY = 1f
        const val scanStepZ = 1f

        const val miniMapPointWidth = 2.2f
        const val miniMapPointHeight = 2.2f
    }

    fun createMinimap(engine: Engine) {
        val physicObject = playerService.constructorMap["ground"]?.getPhysicObject()
        val aabbMin = Vector3()
        val aabbMax = Vector3()
        physicObject?.body?.getAabb(aabbMin, aabbMax)
        physicObject?.dispose()
        val region = scanRegion(aabbMax, aabbMin, engine)

        for (i in region.indices) {
            for (j in region[i].indices) {
                val createEntity = engine.createEntity()
                val color = if (region[i][j] == 1) Color.valueOf("#10571e") else Color.valueOf("#1ca637")
                createEntity.add(ShapeComponent.createComponent(engine, miniMapPointWidth, miniMapPointHeight, color))
                createEntity.add(PositionComponent.createComponent(engine,
                        Matrix4().setTranslation(Vector3(1600f + i * miniMapPointWidth, 0f + j * miniMapPointHeight, 0f))))
                engine.addEntity(createEntity)
            }
        }
    }

    private fun scanRegion(aabbMax: Vector3, aabbMin: Vector3, engine: Engine): Array<IntArray> {
        val halfBox = Vector3(scanStepX, scanStepY, scanStepZ)
        val rateX = (sqrt((aabbMax.x - aabbMin.x).pow(2)) / (halfBox.x * 2)).toInt()
        val rateZ = (sqrt((aabbMax.z - aabbMin.z).pow(2)) / (halfBox.z * 2)).toInt()
        val map = Array(rateX, init = { IntArray(rateZ) })
        for (i in 1..rateX) {
            for (j in 1..rateZ) {
                val cube = createCube(i, j, halfBox, aabbMin, aabbMax, engine)
                engine.addEntity(cube)
                val occupied = physicService.hasCollisions(PhysicComponent.mapper.get(cube).physicObject.body)
                engine.removeEntity(cube)
                map[i - 1][j - 1] = if (occupied) 1 else 0
            }
        }
        return map
    }

    private fun createCube(row: Int, col: Int, halfBox: Vector3, aabbMin: Vector3, aabbMax: Vector3, engine: Engine): Entity {
        val width = halfBox.x
        val depth = halfBox.z
        val position = Vector3(aabbMin.x + row * (width * 2), aabbMax.y - (aabbMax.y - aabbMin.y) / 2, aabbMax.z - col * (depth * 2))
        val matrix4 = Matrix4().setTranslation(position)

        val entity = engine.createEntity()
        entity.add(PositionComponent.createComponent(engine, matrix4))

        val physicComponent = engine.createComponent(PhysicComponent::class.java)
        physicComponent.physicObject = PhysicObject(btRigidBody.btRigidBodyConstructionInfo(1f, null, btBoxShape(halfBox)), "CF_KINEMATIC_OBJECT", 9, listOf(8))
        entity.add(physicComponent)

        return entity
    }
}