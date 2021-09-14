package castle.core.common.`object`

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Disposable

open class CommonEntity : Entity(), Disposable {
    var removed: Boolean = false

    fun add(engine: Engine): CommonEntity {
        engine.addEntity(this)
        return this
    }

    fun remove(engine: Engine): CommonEntity {
        engine.removeEntity(this)
        removed = true
        return this
    }

    override fun dispose() {
        components.forEach {
            if (it is Disposable) {
                it.dispose()
            }
        }
    }
}