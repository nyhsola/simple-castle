package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.scene.game.object.GameSceneObjects;

import java.util.ArrayList;
import java.util.List;

public class GameUnitController {

    private static final Vector3 temVector = new Vector3();

    private final GameSceneObjects gameSceneObjects;
    private final List<UnitGameObject> unitGameObjects = new ArrayList<>();

    public GameUnitController(GameSceneObjects gameSceneObjects) {
        this.gameSceneObjects = gameSceneObjects;
    }

    public void addUnit(UnitGameObject unitGameObject) {
        unitGameObjects.add(unitGameObject);
    }

    public void update() {

    }

}