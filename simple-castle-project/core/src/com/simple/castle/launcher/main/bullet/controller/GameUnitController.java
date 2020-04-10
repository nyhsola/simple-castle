package com.simple.castle.launcher.main.bullet.controller;

import com.simple.castle.launcher.main.bullet.object.unit.UnitGameObject;

import java.util.ArrayList;
import java.util.List;

public class GameUnitController {

    private List<UnitGameObject> unitGameObjects = new ArrayList<>();

    public void addUnit(UnitGameObject unitGameObject) {
        unitGameObjects.add(unitGameObject);
    }

    public void update() {

    }
}
