package com.simple.castle.server.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.server.composition.BaseObject;
import com.simple.castle.server.composition.Constructor;

public class PlayerUnit extends BaseObject {

    private static final int DEFAULT_SPEED_ROTATION = 3;
    private static final int DEFAULT_SPEED_MOVEMENT = 5;
    private static final int DEFAULT_SPEED_MOVEMENT_ON_ROTATION = 5;

    private static final Vector3 ANGULAR_FACTOR = new Vector3(0, 1, 0);
    private static final Vector3 FACE_DIRECTION = new Vector3(1, 0, 0);
    private static final Vector3 ROTATE_LEFT = Vector3.Y.cpy().scl(DEFAULT_SPEED_ROTATION);
    private static final Vector3 ROTATE_RIGHT = Vector3.Y.cpy().scl(-DEFAULT_SPEED_ROTATION);

    private final Vector3 tempVector = new Vector3();
    private final String playerName;

    private Vector3 movePoint;
    private double previousAngle;
    private boolean rotateDirection = false;
    private boolean isDead = false;

    public PlayerUnit(Constructor constructor, Vector3 initPosition, String playerName) {
        super(constructor);
        this.playerName = playerName;

        this.getPhysicObject().getBody().setWorldTransform(new Matrix4());
        this.getPhysicObject().getBody().translate(initPosition);
        this.getPhysicObject().getBody().setAngularFactor(ANGULAR_FACTOR);
    }

    public void update() {
        if (movePoint != null) {
            Vector3 linearVelocity = getLinearVelocity();
            Vector3 angularVelocity = getAngularVelocity(movePoint, linearVelocity);
            if (Vector3.Zero.equals(angularVelocity)) {
                linearVelocity.scl(DEFAULT_SPEED_MOVEMENT);
            } else {
                linearVelocity.scl(DEFAULT_SPEED_MOVEMENT_ON_ROTATION);
            }
            this.getPhysicObject().getBody().setLinearVelocity(linearVelocity);
            this.getPhysicObject().getBody().setAngularVelocity(angularVelocity);
        } else {
            this.getPhysicObject().getBody().setLinearVelocity(Vector3.Zero);
            this.getPhysicObject().getBody().setAngularVelocity(Vector3.Zero);
        }
    }


    private Vector3 getAngularVelocity(Vector3 target, Vector3 linearVelocity) {
        Vector3 angularVelocity = Vector3.Zero;
        Vector3 unitPosition = getModelInstance().transform.getTranslation(tempVector);
        Vector3 targetDirection = target.cpy().sub(unitPosition).nor();

        double currentAngle = getAngle(targetDirection, linearVelocity);

        if (currentAngle <= 0 || currentAngle >= 10) {
            if (currentAngle - previousAngle > 0) {
                rotateDirection = !rotateDirection;
            }
            if (rotateDirection) {
                angularVelocity = ROTATE_LEFT;
            } else {
                angularVelocity = ROTATE_RIGHT;
            }
            previousAngle = currentAngle;
        }
        return angularVelocity;
    }

    private Vector3 getLinearVelocity() {
        return this.getPhysicObject().getBody().getOrientation().transform(FACE_DIRECTION.cpy());
    }

    public void setMovePoint(Vector3 movePoint) {
        this.movePoint = movePoint;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    private double getAngle(Vector3 a, Vector3 b) {
        Vector3 norA = b.cpy().nor();
        Vector3 norB = a.cpy().nor();
        float dot = norB.dot(norA);
        return Math.toDegrees(Math.acos(dot));
    }
}