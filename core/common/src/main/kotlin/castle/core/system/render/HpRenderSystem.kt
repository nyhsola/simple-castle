package castle.core.system.render

import castle.core.component.UnitComponent
import castle.core.component.render.HpRenderComponent
import castle.core.service.CameraService
import castle.core.service.UIService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector3
import org.koin.core.annotation.Single

@Single
class HpRenderSystem(
    private val uiService: UIService,
    private val cameraService: CameraService
) : IteratingSystem(Family.all(UnitComponent::class.java, HpRenderComponent::class.java).get()), EntityListener {
    private val tempVector3: Vector3 = Vector3()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(family, this)
        super.addedToEngine(engine)
    }

    override fun entityAdded(entity: Entity) {
        val hpRenderComponent = HpRenderComponent.mapper.get(entity)
        uiService.addHp(hpRenderComponent.hpBar)
    }

    override fun entityRemoved(entity: Entity) {
        val hpRenderComponent = HpRenderComponent.mapper.get(entity)
        uiService.removeHp(hpRenderComponent.hpBar)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val unitComponent = UnitComponent.mapper.get(entity)
        val hpRenderComponent = HpRenderComponent.mapper.get(entity)
        val position = hpRenderComponent.matrix4.getTranslation(tempVector3).add(hpRenderComponent.translation)
        val camera = cameraService.currentCamera.camera
        val pos = camera.project(position)
        val width = hpRenderComponent.hpBar.width
        hpRenderComponent.hpBar.hpBarRender.setPosition(pos.x - width / 2, pos.y)
    }
}