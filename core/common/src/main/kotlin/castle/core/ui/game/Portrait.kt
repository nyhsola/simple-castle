package castle.core.ui.game

import castle.core.component.render.ModelRenderComponent
import castle.core.service.CommonResources
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class Portrait(
    commonResources: CommonResources
) : Image() {
    private val listDrawables: List<String> = listOf("unit-warrior.png", "castle.png")
    private val drawables: Map<String, TextureRegionDrawable> = commonResources.textures
        .filter { listDrawables.contains(it.key) }
        .mapValues { TextureRegionDrawable(TextureRegion(it.value)) }

    fun changePortrait(entity: Entity) {
        isVisible = true
        drawable = drawables[ModelRenderComponent.mapper.get(entity).nodeName + ".png"]
    }

    fun resetPortrait() {
        isVisible = false
    }
}