package castle.server.ashley.systems.adapter

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

abstract class IteratingSystemAdapter(family: Family?) : IteratingSystem(family), BaseAdapter {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
    }
}