package com.simple.castle.server.kt.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.physics.bullet.Bullet
import com.simple.castle.core.kt.render.BaseRenderer
import com.simple.castle.server.kt.screen.GameScreen

class ServerGame(private val modelBatchSupplier: (() -> ModelBatch)) : Game() {
    private lateinit var baseRenderer: BaseRenderer
    private lateinit var gameScreen: GameScreen

    override fun create() {
        Bullet.init()

        baseRenderer = BaseRenderer(modelBatchSupplier.invoke())
        gameScreen = GameScreen(baseRenderer)

        setScreen(gameScreen)

        Gdx.input.inputProcessor = gameScreen
    }

    override fun dispose() {
        super.dispose()
        gameScreen.dispose()
        baseRenderer.dispose()
    }

}