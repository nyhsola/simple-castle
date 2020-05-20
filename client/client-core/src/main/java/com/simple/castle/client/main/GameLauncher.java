package com.simple.castle.client.main;

import com.badlogic.gdx.Game;
import com.simple.castle.client.game.GameScreen;

public class GameLauncher extends Game {
    private GameScreen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen();
        this.setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        gameScreen.dispose();
    }

}