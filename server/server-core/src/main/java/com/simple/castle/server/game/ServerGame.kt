package com.simple.castle.server.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.physics.bullet.Bullet
import com.simple.castle.core.ServerState
import com.simple.castle.core.render.BaseRenderer
import com.simple.castle.server.screen.GameScreen

class ServerGame(private val batchSupplier: (() -> ModelBatch)) : Game() {
    private var baseRenderer: BaseRenderer? = null
    private var gameScreen: GameScreen? = null

    override fun create() {
        Bullet.init()
        baseRenderer = BaseRenderer(batchSupplier.invoke())
        gameScreen = GameScreen(baseRenderer)
        setScreen(gameScreen)
        Gdx.input.inputProcessor = gameScreen
    }

    override fun dispose() {
        super.dispose()
        gameScreen!!.dispose()
        baseRenderer!!.dispose()
    }

    val state: ServerState
        get() = ServerState(gameScreen!!.state)

}