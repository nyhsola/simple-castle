package castle.core.game.system

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.PositionComponent
import castle.core.common.config.GUIConfig
import castle.core.game.component.HPComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.math.Vector3

class HPSystem(guiConfig: GUIConfig) : IteratingSystem(Family.all(PositionComponent::class.java, HPComponent::class.java).get()) {
    private val decalBatch: DecalBatch = guiConfig.decalBatch
    private val temp: Vector3 = Vector3()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        decalBatch.flush()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val hpComponent = HPComponent.mapper.get(entity)
        hpComponent.decal.position = hpComponent.matrix4.getTranslation(temp).add(hpComponent.translation)
        val percent = hpComponent.currentAmount.toFloat() / hpComponent.amount.toFloat()
        hpComponent.decal.textureRegion = hpComponent.animation.getKeyFrame(hpComponent.animation.animationDuration * percent)
        decalBatch.add(hpComponent.decal)
        if (hpComponent.currentAmount <= 0) {
            val commonEntity = entity as CommonEntity
            commonEntity.remove(engine)
        }
    }
}