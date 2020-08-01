package castle.server.ashley.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g3d.ModelInstance

class RenderComponent : Component {
    lateinit var modelInstance: ModelInstance
    var hide: Boolean = false
}