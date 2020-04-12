package com.simple.castle.launcher.main.bullet.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.launcher.main.bullet.object.unit.UnitGameObject;

import java.util.ArrayList;
import java.util.List;

public class GameUnitController {

    private List<UnitGameObject> unitGameObjects = new ArrayList<>();
    Vector3 bodyVelocity = new Vector3(0, 0, 0.5f);


    public void addUnit(UnitGameObject unitGameObject) {
        unitGameObjects.add(unitGameObject);
    }

    public void update() {
//        for (UnitGameObject unitGameObject : unitGameObjects) {
//            unitGameObject.body.applyCentralForce(bodyVelocity);
//        }

    }
}