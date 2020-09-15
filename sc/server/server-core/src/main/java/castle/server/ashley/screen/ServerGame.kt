package castle.server.ashley.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.physics.bullet.Bullet

class ServerGame(modelBatchSupplier: (() -> ModelBatch)) : Game() {
    private val modelBatch: ModelBatch by lazy { modelBatchSupplier.invoke() }
    private val gameScreen: GameScreen by lazy { GameScreen(modelBatch) }

    override fun create() {
        Bullet.init()
        setScreen(gameScreen)
        Gdx.input.inputProcessor = gameScreen
    }

    override fun dispose() {
        super.dispose()
        gameScreen.dispose()
        modelBatch.dispose()
    }

}