package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.unit.abs.AbstractGameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Player {
    private final Vector3 tempVector = new Vector3();

    private final String unitType;
    private final List<List<AbstractGameObject>> paths;
    private final List<Vector3> initPositions;
    private final String playerName;
    private final List<PlayerUnit> units = new ArrayList<>();

    public Player(String unitType, List<List<AbstractGameObject>> paths, String playerName) {
        this.unitType = unitType;
        this.paths = paths;
        this.initPositions = paths.stream()
                .map(path -> path.stream().findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(sceneObject -> sceneObject.transform.getTranslation(tempVector).cpy())
                .collect(Collectors.toList());
        this.playerName = playerName;
    }

    private static AbstractGameObject getNextAvailable(List<AbstractGameObject> list, AbstractGameObject current) {
        int i = list.indexOf(current);
        if (i >= 0 && i < list.size() - 1) {
            i += 1;
        }
        return list.get(i);
    }

    public void update() {
        units.forEach(PlayerUnit::update);
    }

    public void collisionEvent(PlayerUnit unit, AbstractGameObject gameObject) {
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

    public List<PlayerUnit> spawnUnitsOnStartPositions(ObjectConstructors objectConstructors) {
        return initPositions.stream()
                .map(initPosition ->
                        new PlayerUnit(objectConstructors.getConstructor(unitType), initPosition, playerName))
                .peek(units::add)
                .collect(Collectors.toList());
    }

    public boolean isPlayers(PlayerUnit gameObject) {
        return units.contains(gameObject);
    }

    public Collection<PlayerUnit> getUnits() {
        return units;
    }
}
