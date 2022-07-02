package castle.core.builder

import com.badlogic.ashley.core.Entity
import org.koin.core.annotation.Single

@Single
class ProjectileBuilder(
    private val templateBuilder: TemplateBuilder
) {
    fun build(): Entity {
        return templateBuilder.build("PROJECTILE", "projectile")
    }
}