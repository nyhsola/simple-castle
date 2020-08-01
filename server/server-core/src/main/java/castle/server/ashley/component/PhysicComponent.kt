package castle.server.ashley.component

import castle.server.ashley.physic.PhysicObject
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Disposable

class PhysicComponent : Component, Disposable {
    lateinit var physicObject: PhysicObject
    var mass: Float = 0.0f

    override fun dispose() {
        physicObject.dispose()
    }
}