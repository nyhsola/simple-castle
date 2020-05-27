package com.simple.castle.core.api;

import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.core.ServerState;

public class ServerApi implements Disposable {
    private final Socket socket;
    private NetTool netTool;

    public ServerApi(Socket socket) {
        this.socket = socket;
    }

    public ServerState getPositions() {
        if (netTool == null) {
            netTool = new NetTool(socket);
        }
        netTool.write(ServerCommands.GET_CURRENT_POSITIONS);
        return netTool.read();
    }

    @Override
    public void dispose() {
        socket.dispose();
    }
}
