package com.simple.castle.object.unit;

import com.simple.castle.object.GameObjectConstructor;
import com.simple.castle.object.absunit.AbstractGameObject;
import com.simple.castle.object.absunit.ActiveGameObject;

public class UnitGameObject extends ActiveGameObject {

    private AbstractGameObject movingTo;

    public UnitGameObject(GameObjectConstructor gameObjectConstructor) {
        super(gameObjectConstructor);
    }

    public AbstractGameObject getMovingTo() {
        return movingTo;
    }

    public void setMovingTo(AbstractGameObject movingTo) {
        this.movingTo = movingTo;
    }
}
