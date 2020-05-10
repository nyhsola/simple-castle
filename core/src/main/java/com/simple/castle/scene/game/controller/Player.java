package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.core.object.constructors.ObjectConstructors;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private final Vector3 tempVector = new Vector3();
    private final String playerName;
    private final String unitType;
    private final Set<PlayerUnit> units = new HashSet<>();

    private final List<List<AbstractGameObject>> paths;
    private final List<Vector3> initPositions;

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

    public void collisionEvent(PlayerUnit playersUnit, AbstractGameObject anotherObject) {
        if (playersUnit != null && anotherObject != null) {
            paths.stream()
                    .filter(path -> path.contains(anotherObject))
                    .findAny()
                    .map(path -> getNextAvailable(path, anotherObject))
                    .ifPresent(path -> playersUnit.setMovePoint(path.transform.getTranslation(tempVector).cpy()));
            if (anotherObject instanceof PlayerUnit && !isPlayers((PlayerUnit) anotherObject)) {
                playersUnit.setDeath(true);
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

    public Collection<PlayerUnit> getDeadUnitsAndClear() {
        Set<PlayerUnit> deadUnits = units.stream().filter(PlayerUnit::isDead).collect(Collectors.toSet());
        units.removeAll(deadUnits);
        return deadUnits;
    }
}
