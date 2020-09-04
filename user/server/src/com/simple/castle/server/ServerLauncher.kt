package com.simple.castle.server

import castle.server.ashley.screen.ServerApplication
import castle.server.ashley.screen.ServerGame
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
        ServerApplication(
                isGUI = isGUI(args),
                isServer = isServer(args),
                gameSupplier = ::ServerGame,
                conf = LwjglApplicationConfiguration().apply {
                    fullscreen = false
                    width = LwjglApplicationConfiguration.getDesktopDisplayMode().width
                    height = LwjglApplicationConfiguration.getDesktopDisplayMode().height
                })
                .run()
    }

    private fun isGUI(args: Array<String>): Boolean {
        return listOf(args).none { arg -> arg.contains("--no-gui") }
    }

    private fun isServer(args: Array<String>): Boolean {
        return listOf(args).any { arg -> arg.contains("--server") }
    }
}