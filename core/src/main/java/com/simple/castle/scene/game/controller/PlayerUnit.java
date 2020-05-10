package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.core.object.unit.add.ObjectConstructor;
import com.simple.castle.core.object.unit.basic.ActiveGameObject;
import com.simple.castle.core.utils.MyMathUtils;

public class PlayerUnit extends ActiveGameObject {

    private static final int TRIGGER_AREA = 20;
    private static final int DEFAULT_SPEED_MOVEMENT = 5;
    private static final int DEFAULT_SPEED_ON_ROTATION = 5;

    private static final Vector3 FACE_DIRECTION = new Vector3(1, 0, 0);
    private static final Vector3 ROTATE_LEFT = Vector3.Y.cpy().scl(2f);
    private static final Vector3 ROTATE_RIGHT = Vector3.Y.cpy().scl(-2f);

    private final Vector3 tempVector = new Vector3();
    private final String playerName;

    private PlayerUnit enemy;

    private Vector3 movePoint;
    private double previousAngle;
    private boolean rotateDirection = false;
    private boolean death = false;

    public PlayerUnit(ObjectConstructor objectConstructor, Vector3 initPosition, String playerName) {
        super(objectConstructor);
        this.playerName = playerName;
        this.body.setWorldTransform(new Matrix4());
        this.body.translate(initPosition);
    }

    public void enemyDistanceEvent(PlayerUnit enemy, float distance) {
        if (!enemyExist() && distance < TRIGGER_AREA) {
            this.enemy = enemy;
        }
    }

    public void update() {
        boolean enemyExist = enemyExist();
        boolean movePointExist = movePointExist();
        Vector3 aim = null;

        if (enemyExist) {
            aim = enemy.transform.getTranslation(tempVector).cpy();
        } else if (movePointExist) {
            aim = movePoint;
        }

        if (aim != null) {
            adjustMoveAndAngle(aim, DEFAULT_SPEED_ON_ROTATION, DEFAULT_SPEED_MOVEMENT);
        } else {
            this.body.setAngularVelocity(Vector3.Zero);
            this.body.setLinearVelocity(Vector3.Zero);
        }
    }

    private void adjustMoveAndAngle(Vector3 point, int speedOnRotate, int unitSpeed) {
        Vector3 unitV = this.transform.getTranslation(tempVector);
        Vector3 targetDirection = point.cpy().sub(unitV).nor();
        Vector3 forwardDirection = getForwardDirection();

        double currentAngle = MyMathUtils.getAngle(targetDirection, forwardDirection);

        if (currentAngle <= 0 || currentAngle >= 5) {
            if (currentAngle - previousAngle > 0) {
                rotateDirection = !rotateDirection;
            }
            if (rotateDirection) {
                this.body.setAngularVelocity(ROTATE_LEFT);
            } else {
                this.body.setAngularVelocity(ROTATE_RIGHT);
            }
            previousAngle = currentAngle;
            this.body.setLinearVelocity(forwardDirection.scl(speedOnRotate));
        } else {
            this.body.setAngularVelocity(Vector3.Zero);
            this.body.setLinearVelocity(forwardDirection.scl(unitSpeed));
        }
    }

    private boolean enemyExist() {
        return enemy != null && !enemy.isDead();
    }

    private boolean movePointExist() {
        return movePoint != null;
    }

    private Vector3 getForwardDirection() {
        return this.body.getOrientation().transform(FACE_DIRECTION.cpy());
    }

    public void setMovePoint(Vector3 movePoint) {
        this.movePoint = movePoint;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isDead() {
        return death;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }
}
