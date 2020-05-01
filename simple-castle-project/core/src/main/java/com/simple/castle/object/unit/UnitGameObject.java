package com.simple.castle.object.unit;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.object.constructors.ObjectConstructor;
import com.simple.castle.object.unit.absunit.ActiveGameObject;

public class UnitGameObject extends ActiveGameObject {

    private static final int UNIT_DEFAULT_SPEED = 5;
    private final Vector3 tempVector = new Vector3();
    private Vector3 target;

    public UnitGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor);
    }

    public void updateTarget() {
        if (target != null) {
            Vector3 unitV = this.transform.getTranslation(tempVector);
            this.body.setLinearVelocity(target.cpy().sub(unitV).nor().scl(UNIT_DEFAULT_SPEED));
            this.body.setAngularVelocity(new Vector3(0, 0, 1));
            this.body.getOrientation();


        }
    }

    public Vector3 getTarget() {
        return target;
    }

    public void setTarget(Vector3 target) {
        this.target = target;
    }
}
