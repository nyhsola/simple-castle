package castle.core.util

import com.badlogic.gdx.graphics.Color

interface ColorUtil {
    companion object {
        fun dimmedColor(baseColor: Color, dimmed: Float): Color {
            return Color(
                baseColor.r * dimmed,
                baseColor.g * dimmed,
                baseColor.b * dimmed,
                baseColor.a)
        }
    }
}