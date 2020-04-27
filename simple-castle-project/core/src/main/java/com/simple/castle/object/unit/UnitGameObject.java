package com.simple.castle.object.unit;

import com.simple.castle.object.GameObjectConstructor;
import com.simple.castle.object.absunit.ActiveGameObject;

public class UnitGameObject extends ActiveGameObject {

    private boolean moving = false;

    public UnitGameObject(GameObjectConstructor gameObjectConstructor) {
        super(gameObjectConstructor);
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
