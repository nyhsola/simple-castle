package castle.core.system.render

import castle.core.component.UnitComponent
import castle.core.component.render.HPRenderComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.math.Vector3

class HpRenderSystem(private val decalBatch: DecalBatch) : IteratingSystem(Family.all(UnitComponent::class.java, HPRenderComponent::class.java).get()) {
    private val temp: Vector3 = Vector3()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        decalBatch.flush()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val hpRenderComponent = HPRenderComponent.mapper.get(entity)
        val position = hpRenderComponent.matrix4.getTranslation(temp).add(hpRenderComponent.translation)
        val percent = unitComponent.currentHealth.toFloat() / unitComponent.totalHealth.toFloat()
        val textureRegion = hpRenderComponent.animation.getKeyFrame(hpRenderComponent.animation.animationDuration * percent)
        hpRenderComponent.decal.textureRegion = textureRegion
        hpRenderComponent.decal.position = position
        decalBatch.add(hpRenderComponent.decal)
    }
}