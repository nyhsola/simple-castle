package com.simple.castle.launcher.main.bullet.scene.game.physic;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.launcher.main.bullet.object.AbstractGameObject;
import com.simple.castle.launcher.main.bullet.render.GameCamera;

public class GameSceneWorld extends InputAdapter implements Disposable {

    private final GameScenePhysic gameScenePhysic;

    public GameSceneWorld() {
        this.gameScenePhysic = new GameScenePhysic();
    }

    public void addRigidBody(AbstractGameObject object) {
        gameScenePhysic.addRigidBody(object);
    }

    public void update(GameCamera camera, float delta) {
        gameScenePhysic.update(camera, delta);
    }

    @Override
    public void dispose() {
        gameScenePhysic.dispose();
    }
}
