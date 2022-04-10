package castle.core.system

import castle.core.component.TextCountComponent
import castle.core.component.render.TextRenderComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class TextCountSystem : IteratingSystem(family) {
    private companion object {
        private val family: Family = Family.all(TextCountComponent::class.java, TextRenderComponent::class.java).get()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textCountComponent = TextCountComponent.mapper.get(entity)
        val textRenderComponent = TextRenderComponent.mapper.get(entity)
        textRenderComponent.text = "%.0f".format(textCountComponent.left())
        textCountComponent.task.update(deltaTime)
    }
}