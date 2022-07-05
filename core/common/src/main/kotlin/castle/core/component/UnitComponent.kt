package castle.core.component

import castle.core.json.UnitJson
import castle.core.util.UnitUtils
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity

class UnitComponent(
    val owner: Entity,
    val unitJson: UnitJson
) : Component {
    companion object {
        val mapper: ComponentMapper<UnitComponent> = ComponentMapper.getFor(UnitComponent::class.java)
    }

    val params: MutableMap<String, Any> = HashMap()

    val totalHealth: Int = unitJson.amount
    val visibilityRange: Float = unitJson.visibilityRange
    var playerName: String = "neutral"
    var currentHealth = totalHealth

    val inTouchObjects: MutableSet<Entity> = HashSet()

    val isEnemiesInTouch: Boolean
        get() = inTouchEnemies.isNotEmpty()
    val inTouchEnemies: List<UnitComponent>
        get() = UnitUtils.findEnemies(this, UnitUtils.extractUnit(inTouchObjects))
    val isDead: Boolean
        get() = currentHealth <= 0

    fun takeDamage(amountParam: Int) {
        currentHealth = Integer.max(currentHealth - amountParam, 0)
    }
}