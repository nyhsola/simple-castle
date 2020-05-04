package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.object.unit.absunit.AbstractGameObject;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private final Vector3 tempVector = new Vector3();

    private final String unitType;
    private final List<List<AbstractGameObject>> paths;
    private final Map<String, UnitGameObject> units = new HashMap<>();

    public Player(String unitType, List<List<AbstractGameObject>> paths) {
        this.unitType = unitType;
        this.paths = paths;
    }

    private static AbstractGameObject getNextAvailable(List<AbstractGameObject> list, AbstractGameObject current) {
        int i = list.indexOf(current);
        if (i >= 0 && i < list.size() - 1) {
            i += 1;
        }
        return list.get(i);
    }

    public void update() {
        units.forEach((s, unitGameObject) -> unitGameObject.updateTarget());
    }

    public void collisionEvent(UnitGameObject unit, AbstractGameObject gameObject) {
        if (unit != null && gameObject != null) {
            List<AbstractGameObject> path = paths.stream()
                    .filter(gameObjects -> gameObjects.contains(gameObject))
                    .findAny()
                    .orElse(null);

            if (path != null) {
                AbstractGameObject nextAvailable = getNextAvailable(path, gameObject);
                unit.setTarget(nextAvailable.transform.getTranslation(tempVector).cpy());
            }
        }
    }

    public List<UnitGameObject> spawnUnitsOnStartPositions(ObjectConstructors objectConstructors) {
        return paths.stream()
                .map(strings -> strings.stream().findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(sceneObject -> sceneObject.transform.getTranslation(tempVector).cpy())
                .map(vector3 -> {
                    UnitGameObject unit = new UnitGameObject(objectConstructors.getConstructor(unitType));
                    unit.body.setWorldTransform(new Matrix4());
                    unit.body.translate(vector3);
                    unit.body.userData = UUID.randomUUID().toString();
                    return unit;
                })
                .peek(unit -> {
                    units.put((String) unit.body.userData, unit);
                })
                .collect(Collectors.toList());
    }

    public boolean isPlayers(String objectName) {
        return units.containsKey(objectName);
    }
}
