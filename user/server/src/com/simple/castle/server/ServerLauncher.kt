package com.simple.castle.server

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.simple.castle.server.kt.game.ServerApplication
import com.simple.castle.server.kt.game.ServerGame

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")

        ServerApplication.Builder()
                .enableGUI(isGUI(args))
                .enableServer(isServer(args))
                .setConf(LwjglApplicationConfiguration().apply {
                    fullscreen = false
                    width = LwjglApplicationConfiguration.getDesktopDisplayMode().width
                    height = LwjglApplicationConfiguration.getDesktopDisplayMode().height
                })
                .setGameSupplier { modelBatchSupplier: () -> ModelBatch -> ServerGame(modelBatchSupplier) }
                .build()
                .run()
    }

    private fun isGUI(args: Array<String>): Boolean {
        return listOf(args).none { arg -> arg.contains("--no-gui") }
    }

    private fun isServer(args: Array<String>): Boolean {
        return listOf(args).any { arg -> arg.contains("--server") }
    }
}