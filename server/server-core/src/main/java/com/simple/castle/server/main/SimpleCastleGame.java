package com.simple.castle.server.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.simple.castle.base.World;
import com.simple.castle.server.main.asset.AssetLoader;
import com.simple.castle.server.main.physic.GroundObject;
import com.simple.castle.server.main.physic.PhysicWorld;
import com.simple.castle.server.tcp.DataListener;

public final class SimpleCastleGame implements ApplicationListener, InputProcessor {
    private PhysicWorld physicWorld;
    private GroundObject groundObject;

    private Model model;

    private final Filler filler = new Filler();
    private final World world = new World();

    private final DataListener dataListener;

    public SimpleCastleGame(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    @Override
    public void create() {
        Bullet.init();

        physicWorld = new PhysicWorld();
        model = new AssetLoader().loadModel();

        Node groundNode = model.getNode("ground");
        btCollisionShape collisionShape = Bullet.obtainStaticNodeShape(groundNode, false);

        groundObject = new GroundObject(
                new btRigidBody.btRigidBodyConstructionInfo(0, null, collisionShape));
        physicWorld.addRigidBody(
                groundObject);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        physicWorld.update(Gdx.graphics.getDeltaTime());

        filler.fillWorld(world, groundObject);
        dataListener.worldTick(world);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        model.dispose();
        physicWorld.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}