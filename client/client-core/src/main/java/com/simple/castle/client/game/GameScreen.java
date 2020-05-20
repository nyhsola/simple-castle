package com.simple.castle.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.simple.castle.base.World;
import com.simple.castle.client.server.ServerConnector;

public class GameScreen extends ScreenAdapter {
    private final ServerConnector serverConnector;

    public GameScreen() {
        this.serverConnector = new ServerConnector("127.0.0.1", 9090);
    }

    @Override
    public void render(float delta) {
        World nextWorld = serverConnector.getNextWorldTick();

        Gdx.app.log("world", nextWorld + " " + 1.0 / delta);
    }

    @Override
    public void dispose() {
        serverConnector.dispose();
    }
}
