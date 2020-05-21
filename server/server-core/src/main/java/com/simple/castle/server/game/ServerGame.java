package com.simple.castle.server.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.simple.castle.server.render.BaseRenderer;
import com.simple.castle.server.screen.ServerScreen;
import com.simple.castle.server.tcp.DataListener;

public final class ServerGame extends Game {

    private final boolean isGUI;
    private final DataListener dataListener;

    private BaseRenderer baseRenderer;
    private ServerScreen serverScreen;

    public ServerGame(boolean isGUI, DataListener dataListener) {
        this.isGUI = isGUI;
        this.dataListener = dataListener;
    }

    @Override
    public void create() {
        Bullet.init();
        baseRenderer = new BaseRenderer(isGUI);
        serverScreen = new ServerScreen(baseRenderer);
        serverScreen.addDataListener(dataListener);

        this.setScreen(serverScreen);

        Gdx.input.setInputProcessor(serverScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        serverScreen.dispose();
        baseRenderer.dispose();
    }

}