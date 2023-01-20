package castle.core.builder

import castle.core.component.RemoveComponent
import com.badlogic.ashley.core.Entity
import org.koin.core.annotation.Single

@Single
class ProjectileBuilder(
        private val templateBuilder: TemplateBuilder
) {
    fun build(): Entity {
        val entity = templateBuilder.build("PROJECTILE", "projectile")
        entity.add(RemoveComponent(1f))
        return entity
    }
}