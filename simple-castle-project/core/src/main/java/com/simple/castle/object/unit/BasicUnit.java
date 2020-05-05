package com.simple.castle.object.unit;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.object.unit.add.ObjectConstructor;
import com.simple.castle.object.unit.basic.ActiveGameObject;

public class BasicUnit extends ActiveGameObject {

    private static final int UNIT_DEFAULT_SPEED = 5;
    private static final Vector3 faceDirection = new Vector3(1, 0, 0);
    private static final Vector3 rotateL = new Vector3(0, 2, 0);
    private static final Vector3 rotateR = new Vector3(0, -2, 0);

    private final Vector3 tempVector = new Vector3();

    private Vector3 movePoint;
    private double previousAngle;
    private boolean rotateDirection = false;
    private boolean attackMode = false;

    public BasicUnit(ObjectConstructor objectConstructor, Vector3 initPosition) {
        super(objectConstructor);

        this.body.setWorldTransform(new Matrix4());
        this.body.translate(initPosition);
    }

    public void update() {
        if (movePoint != null && !attackMode) {
            Vector3 unitV = this.transform.getTranslation(tempVector);
            Vector3 targetDirection = movePoint.cpy().sub(unitV).nor();
            this.body.setLinearVelocity(targetDirection.scl(UNIT_DEFAULT_SPEED));

            double currentAngle = getAngleBetweenVectors(targetDirection, getModelForwardDirection());
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
            } else {
                this.body.setAngularVelocity(Vector3.Zero);
            }
        }
    }

    public void unitNear(BasicUnit nearUnit, float distance) {
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
}
