package com.simple.castle.server.physic.unit;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class GroundObject extends KinematicObject {

    private final Vector3 position = new Vector3();

    public GroundObject(btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(constructionInfo);
    }

    public Vector3 getPosition() {
        return body.getWorldTransform().getTranslation(position);
    }
}
