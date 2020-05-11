package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.core.object.unit.add.ObjectConstructor;
import com.simple.castle.core.object.unit.basic.ActiveGameObject;
import com.simple.castle.core.utils.CastleMathUtils;

public class PlayerUnit extends ActiveGameObject {

    private static final int DEFAULT_SPEED_MOVEMENT = 5;
    private static final int DEFAULT_SPEED_MOVEMENT_ON_ROTATION = 4;
    private static final float DEFAULT_SPEED_ROTATION = 3;

    private static final Vector3 FACE_DIRECTION = new Vector3(1, 0, 0);
    private static final Vector3 ROTATE_LEFT = Vector3.Y.cpy().scl(DEFAULT_SPEED_ROTATION);
    private static final Vector3 ROTATE_RIGHT = Vector3.Y.cpy().scl(-DEFAULT_SPEED_ROTATION);

    private final Vector3 tempVector = new Vector3();
    private final String playerName;

    private PlayerUnit enemy;
    private Vector3 movePoint;
    private double previousAngle;
    private boolean rotateDirection = false;
    private boolean isDead = false;

    public PlayerUnit(ObjectConstructor objectConstructor, Vector3 initPosition, String playerName) {
        super(objectConstructor);
        this.playerName = playerName;
        this.body.setWorldTransform(new Matrix4());
        this.body.translate(initPosition);
    }

    public void enemyDistanceEvent(PlayerUnit enemy, float distance) {
        if (!enemyExist()) {
            this.enemy = enemy;
        }
    }

    public void update() {
        Vector3 target = null;
        boolean enemyExist = enemyExist();
        boolean movePointExist = movePointExist();

        if (enemyExist) {
            target = enemy.transform.getTranslation(tempVector).cpy();
        }

        if (movePointExist && !enemyExist) {
            target = movePoint;
        }

        if (target != null) {
            Vector3 linearVelocity = getLinearVelocity();
            Vector3 angularVelocity = getAngularVelocity(target, linearVelocity);

            if (Vector3.Zero.equals(angularVelocity)) {
                linearVelocity.scl(DEFAULT_SPEED_MOVEMENT);
            } else {
                linearVelocity.scl(DEFAULT_SPEED_MOVEMENT_ON_ROTATION);
            }

            this.body.setLinearVelocity(linearVelocity);
            this.body.setAngularVelocity(angularVelocity);
        } else {
            this.body.setLinearVelocity(Vector3.Zero);
            this.body.setAngularVelocity(Vector3.Zero);
        }
    }


    private Vector3 getAngularVelocity(Vector3 target, Vector3 linearVelocity) {
        Vector3 angularVelocity = Vector3.Zero;
        Vector3 unitPosition = transform.getTranslation(tempVector);
        Vector3 targetDirection = target.cpy().sub(unitPosition).nor();

        double currentAngle = CastleMathUtils.getAngle(targetDirection, linearVelocity);

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

    private boolean enemyExist() {
        return enemy != null && !enemy.isDead();
    }

    private boolean movePointExist() {
        return movePoint != null;
    }

    private Vector3 getLinearVelocity() {
        return this.body.getOrientation().transform(FACE_DIRECTION.cpy());
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
}
