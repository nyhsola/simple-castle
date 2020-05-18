package com.simple.castle.server.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.simple.castle.base.Ground;
import com.simple.castle.base.Position;
import com.simple.castle.base.World;
import com.simple.castle.server.main.asset.AssetLoader;
import com.simple.castle.server.main.physic.GroundObject;
import com.simple.castle.server.main.physic.PhysicWorld;

public final class GameServer implements ApplicationListener {
    private PhysicWorld physicWorld;
    private GroundObject groundObject;


    private Model model;
    private World world;
    private Ground ground;
    private Position position;

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

        world = new World();
        ground = new Ground();
        position = new Position();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        physicWorld.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        physicWorld.dispose();
    }

    public World getWorld() {
        Vector3 positionV = groundObject.getPosition();
        position.setX(positionV.x);
        position.setY(positionV.y);
        position.setZ(positionV.z);

        ground.setPosition(position);
        world.setGround(ground);
        return world;
    }

}