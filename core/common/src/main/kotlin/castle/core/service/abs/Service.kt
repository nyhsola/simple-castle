package castle.core.service.abs

import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

interface Service : KtxInputAdapter, Disposable {
    fun priority(): Int = 9
    fun init() {}
    fun update(deltaTime: Float) {}
    fun updateTick(deltaTime: Float) {}
    fun resize(width: Int, height: Int) {}
    override fun dispose() {}
}