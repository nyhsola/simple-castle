package castle.core.game.system

import castle.core.game.component.TextComponent
import castle.core.game.component.TextCountComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class TextCountSystem : IteratingSystem(family) {
    private companion object {
        private val family: Family = Family.all(TextCountComponent::class.java, TextComponent::class.java).get()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textCountComponent = TextCountComponent.mapper.get(entity)
        val textComponent = TextComponent.mapper.get(entity)
        textComponent.text = "%.0f".format(textCountComponent.left())
        textCountComponent.task.update(deltaTime)
    }
}