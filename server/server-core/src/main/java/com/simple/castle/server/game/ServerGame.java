package com.simple.castle.server.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.simple.castle.server.tcp.DataListener;

public final class ServerGame extends Game {

    private final DataListener dataListener;

    private ServerScreen serverScreen;

    public ServerGame(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    @Override
    public void create() {
        Bullet.init();

        serverScreen = new ServerScreen(dataListener);
        this.setScreen(serverScreen);

        Gdx.input.setInputProcessor(serverScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        serverScreen.dispose();
    }

}