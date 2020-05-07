package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.object.unit.add.ObjectConstructor;
import com.simple.castle.object.unit.basic.ActiveGameObject;

public class PlayerUnit extends ActiveGameObject {

    private static final int UNIT_DEFAULT_SPEED = 5;
    private static final int UNIT_SPEED_WHEN_ROTATING = 2;
    private static final Vector3 faceDirection = new Vector3(1, 0, 0);
    private static final Vector3 rotateL = Vector3.Y.cpy().scl(2f);
    private static final Vector3 rotateR = Vector3.Y.cpy().scl(-2f);

    private final Vector3 tempVector = new Vector3();
    private final String playerName;

    private Vector3 movePoint;
    private double previousAngle;
    private boolean rotateDirection = false;
    private boolean attackMode = false;

    public PlayerUnit(ObjectConstructor objectConstructor, Vector3 initPosition, String playerName) {
        super(objectConstructor);
        this.playerName = playerName;
        this.body.setWorldTransform(new Matrix4());
        this.body.translate(initPosition);
    }

    public void update() {
        if (isMoving()) {
            Vector3 unitV = this.transform.getTranslation(tempVector);
            Vector3 targetDirection = movePoint.cpy().sub(unitV).nor();
            Vector3 modelForwardDirection = getModelForwardDirection();

            double currentAngle = getAngleBetweenVectors(targetDirection, modelForwardDirection);

            if (currentAngle <= 0 || currentAngle >= 5) {
                if (currentAngle - previousAngle > 0) {
                    rotateDirection = !rotateDirection;
                }
                if (rotateDirection) {
                    this.body.setAngularVelocity(rotateL);
                } else {
                    this.body.setAngularVelocity(rotateR);
                }
                previousAngle = currentAngle;
                this.body.setLinearVelocity(modelForwardDirection.scl(UNIT_SPEED_WHEN_ROTATING));
            } else {
                this.body.setAngularVelocity(Vector3.Zero);
                this.body.setLinearVelocity(modelForwardDirection.scl(UNIT_DEFAULT_SPEED));
            }
        } else {
            this.body.setAngularVelocity(Vector3.Zero);
            this.body.setLinearVelocity(Vector3.Zero);
        }
    }

    public boolean isMoving() {
        return movePoint != null && !attackMode;
    }

    public void unitNear(PlayerUnit nearUnit, float distance) {
        if (distance < 5) {
            attackMode = true;
        }
    }

    private Vector3 getModelForwardDirection() {
        return this.body.getOrientation().transform(faceDirection.cpy());
    }

    private double getAngleBetweenVectors(Vector3 a, Vector3 b) {
        Vector3 norA = b.cpy().nor();
        Vector3 norB = a.cpy().nor();
        float dot = norB.dot(norA);
        return Math.toDegrees(Math.acos(dot));
    }

    public Vector3 getMovePoint() {
        return movePoint;
    }

    public void setMovePoint(Vector3 movePoint) {
        this.movePoint = movePoint;
    }

    public String getPlayerName() {
        return playerName;
    }
}
