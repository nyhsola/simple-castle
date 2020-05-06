package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.unit.BasicUnit;
import com.simple.castle.object.unit.abs.AbstractGameObject;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private final Vector3 tempVector = new Vector3();

    private final String unitType;
    private final List<List<AbstractGameObject>> paths;
    private final List<Vector3> initPositions;
    private final Map<String, BasicUnit> units = new HashMap<>();

    public Player(String unitType, List<List<AbstractGameObject>> paths) {
        this.unitType = unitType;
        this.paths = paths;
        this.initPositions = paths.stream()
                .map(path -> path.stream().findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(sceneObject -> sceneObject.transform.getTranslation(tempVector).cpy())
                .collect(Collectors.toList());
    }

    private static AbstractGameObject getNextAvailable(List<AbstractGameObject> list, AbstractGameObject current) {
        int i = list.indexOf(current);
        if (i >= 0 && i < list.size() - 1) {
            i += 1;
        }
        return list.get(i);
    }

    public void update() {
        units.forEach((s, basicUnit) -> basicUnit.update());
    }

    public void collisionEvent(BasicUnit unit, AbstractGameObject gameObject) {
        if (unit != null && gameObject != null) {
            List<AbstractGameObject> path = paths.stream()
                    .filter(gameObjects -> gameObjects.contains(gameObject))
                    .findAny()
                    .orElse(null);

            if (path != null) {
                AbstractGameObject nextAvailable = getNextAvailable(path, gameObject);
                Vector3 movePoint = nextAvailable.transform.getTranslation(tempVector).cpy();
                unit.setMovePoint(movePoint);
            }
        }
    }

    public List<BasicUnit> spawnUnitsOnStartPositions(ObjectConstructors objectConstructors) {
        return initPositions
                .stream()
                .map(initPosition -> new BasicUnit(objectConstructors.getConstructor(unitType), initPosition))
                .peek(unit -> units.put((String) unit.body.userData, unit))
                .collect(Collectors.toList());
    }

    public boolean isPlayers(AbstractGameObject gameObject) {
        return units.containsKey(String.valueOf(gameObject.body.userData));
    }

    public Collection<BasicUnit> getUnits() {
        return units.values();
    }
}
