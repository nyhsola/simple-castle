package castle.core.game.path

import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class AreaHeuristic : Heuristic<Area> {
    override fun estimate(from: Area, to: Area): Float {
        return Vector2.dst(from.x.toFloat(), from.y.toFloat(), to.x.toFloat(), to.y.toFloat())
    }
}