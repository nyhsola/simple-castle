package com.simple.castle.core.kt.api

import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.utils.Disposable

class ServerApi(private val socket: Socket) : Disposable {
    private var netTool: NetTool? = null
    val positions: ServerState?
        get() {
            if (netTool == null) {
                netTool = NetTool(socket)
            }
            netTool!!.write<Byte>(ServerCommands.GET_CURRENT_POSITIONS)
            return netTool!!.read<ServerState>()
        }

    override fun dispose() {
        socket.dispose()
    }

}