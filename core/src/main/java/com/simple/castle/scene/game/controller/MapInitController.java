package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.core.event.CollisionEvent;
import com.simple.castle.core.event.Done;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.physic.PhysicEngine;


// TODO: 5/15/2020 Delete this
public class MapInitController implements Disposable, CollisionEvent, Done {

    private static final btBoxShape COLLISION_SHAPE = new btBoxShape(new Vector3(1, 1, 1));
    private static final int byX = 100;
    private static final int byZ = 100;

//    private final PhysicEngine physicEngine;

    public MapInitController(PhysicEngine physicEngine) {
//        this.physicEngine = physicEngine;
//
//        float sizeByX = COLLISION_SHAPE.getHalfExtentsWithMargin().x * 2;
//        float fromX = -(sizeByX * byX) / 2;
//
//        float sizeByZ = COLLISION_SHAPE.getHalfExtentsWithMargin().z * 2;
//        float fromZ = -(sizeByZ * byZ) / 2;
//
//        float fromZI = fromZ;
//
//        for (int i = 0; i < byX; i++) {
//            for (int j = 0; j < byZ; j++) {
//                btRigidBody bodyTest = new btRigidBody(new btRigidBody.btRigidBodyConstructionInfo(
//                        0, null, COLLISION_SHAPE, Vector3.Zero));
//                Matrix4 translate = bodyTest.getWorldTransform().translate(new Vector3(fromX, 0, fromZ));
//                bodyTest.setWorldTransform(translate);
//                bodyTest.setCollisionFlags(bodyTest.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
//                physicEngine.addRigidBody(bodyTest);
//                fromZ = fromZ + sizeByZ;
//            }
//            fromX = fromX + sizeByX;
//            fromZ = fromZI;
//        }
    }

    @Override
    public void update() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void collisionEvent(AbstractGameObject object1, AbstractGameObject object2) {

    }

    @Override
    public boolean isDone() {
        return true;
    }

}
