package castle.server.ashley.path

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.math.Vector2

class AreaConnection(private val from: Area, private val to: Area) : Connection<Area> {
    private val cost: Float = Vector2.dst(from.indexPosition.x, from.indexPosition.y, to.indexPosition.x, to.indexPosition.y)

    override fun getCost(): Float {
        return cost
    }

    override fun getFromNode(): Area {
        return from
    }

    override fun getToNode(): Area {
        return to
    }
}