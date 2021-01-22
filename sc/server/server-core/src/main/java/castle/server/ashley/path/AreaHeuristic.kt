package castle.server.ashley.path

import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.math.Vector2

class AreaHeuristic : Heuristic<Area> {
    override fun estimate(from: Area, to: Area): Float {
        return Vector2.dst(from.indexPosition.x, from.indexPosition.y, to.indexPosition.x, to.indexPosition.y)
    }
}