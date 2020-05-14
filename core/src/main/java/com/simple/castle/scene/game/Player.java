package com.simple.castle.scene.game;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.core.object.constructors.ObjectConstructors;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.utils.CastleListUtils;
import com.simple.castle.scene.game.unit.PlayerUnit;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private final Vector3 tempVector = new Vector3();
    private final String playerName;
    private final String unitType;
    private final Set<com.simple.castle.scene.game.unit.PlayerUnit> units = new HashSet<>();

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

    public void update() {
        units.forEach(com.simple.castle.scene.game.unit.PlayerUnit::update);
    }

    public void collisionEvent(com.simple.castle.scene.game.unit.PlayerUnit playersUnit, AbstractGameObject anotherObject) {
        if (playersUnit != null && anotherObject != null) {
            paths.stream()
                    .filter(path -> path.contains(anotherObject))
                    .findAny()
                    .map(path -> CastleListUtils.getNextAvailable(path, anotherObject))
                    .ifPresent(path -> playersUnit.setMovePoint(path.transform.getTranslation(tempVector).cpy()));
            if (anotherObject instanceof com.simple.castle.scene.game.unit.PlayerUnit && !isPlayers((com.simple.castle.scene.game.unit.PlayerUnit) anotherObject)) {
                playersUnit.setDead(true);
            }
        }
    }

    public List<com.simple.castle.scene.game.unit.PlayerUnit> spawnUnitsOnStartPositions(ObjectConstructors objectConstructors) {
        return initPositions.stream()
                .map(initPosition ->
                        new com.simple.castle.scene.game.unit.PlayerUnit(objectConstructors.getConstructor(unitType), initPosition, playerName))
                .peek(units::add)
                .collect(Collectors.toList());
    }

    public boolean isPlayers(com.simple.castle.scene.game.unit.PlayerUnit gameObject) {
        return units.contains(gameObject);
    }

    public Collection<com.simple.castle.scene.game.unit.PlayerUnit> getUnits() {
        return units;
    }

    public Collection<com.simple.castle.scene.game.unit.PlayerUnit> getDeadUnitsAndClear() {
        Set<com.simple.castle.scene.game.unit.PlayerUnit> deadUnits = units.stream().filter(PlayerUnit::isDead).collect(Collectors.toSet());
        units.removeAll(deadUnits);
        return deadUnits;
    }
}
