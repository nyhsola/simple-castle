package com.simple.castle.server.main.unit;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PlayerUnit extends ActiveUnit {
    private static final Vector3 ANGULAR_FACTOR = new Vector3(0, 1, 0);

    private final Vector3 initPosition;

    public PlayerUnit(btRigidBody.btRigidBodyConstructionInfo constructionInfo, Vector3 initPosition) {
        super(constructionInfo);
        this.initPosition = initPosition;

        this.body.setWorldTransform(new Matrix4());
        this.body.translate(initPosition);

        this.body.setAngularFactor(ANGULAR_FACTOR);
    }
}
