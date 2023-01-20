package castle.core.physic

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import java.util.function.Function

enum class PhysicShape(private val function: Function<ModelInstance, btCollisionShape>) {
    STATIC(Function<ModelInstance, btCollisionShape> { node -> PhysicShapeInit.calculateStaticNodeShape(node) }),
    BASE_BOX(Function<ModelInstance, btCollisionShape> { node -> PhysicShapeInit.calculateBaseBox(node) }),
    ADJUSTED_BASE_BOX(Function<ModelInstance, btCollisionShape> { node -> PhysicShapeInit.calculateAdjustedBox(node) });

    fun build(modelInstance: ModelInstance): btCollisionShape {
        return function.apply(modelInstance)
    }
}