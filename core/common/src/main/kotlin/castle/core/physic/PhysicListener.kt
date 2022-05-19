package castle.core.physic

import com.badlogic.ashley.core.Entity

interface PhysicListener {
    fun onContactStarted(entity1: Entity, entity2: Entity)
    fun onContactEnded(entity1: Entity, entity2: Entity)
}