package castle.core.util

import com.badlogic.gdx.utils.TimeUtils

abstract class DoubleTap(private val keyTap: Int, private val timeNeedMillis: Int) {
    private var lastSpacePress = 0L

    fun keyDown(keycode: Int): Boolean {
        if (keycode == keyTap) {
            if (TimeUtils.millis() - lastSpacePress <= timeNeedMillis) {
                onDoubleTap()
            }
            lastSpacePress = TimeUtils.millis()
        }
        return false
    }

    abstract fun onDoubleTap()
}