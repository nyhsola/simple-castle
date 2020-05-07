package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.event.DoEvery;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.listener.SceneObjectManager;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.utils.jsondto.PlayerJson;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerController implements CollisionEvent, Disposable {

    public static final long spawnEvery = 3 * 1000;
    public static final long triggerDistanceEvery = 3 * 1000;
    public static final long updateUnitsEvery = 500;

    private static final Vector3 tempVector1 = new Vector3();
    private static final Vector3 tempVector2 = new Vector3();

    private final ObjectConstructors objectConstructors;
    private final SceneObjectManager sceneObjectManager;
    private final List<Player> players;

    private final DoEvery spawnUnits = new DoEvery(spawnEvery, true);
    private final DoEvery distanceRecalculate = new DoEvery(triggerDistanceEvery, true);
    private final DoEvery updateUnits = new DoEvery(updateUnitsEvery, true);

    private PlayerController(List<Player> players, ObjectConstructors objectConstructors,
                             SceneObjectManager sceneObjectManager) {
        this.objectConstructors = objectConstructors;
        this.sceneObjectManager = sceneObjectManager;
        this.players = players;
    }

    @Override
    public void collisionEvent(btCollisionObject object1, btCollisionObject object2) {
        Object userDataObj1 = object1.userData;
        Object userDataObj2 = object2.userData;
        if (userDataObj1 instanceof AbstractGameObject && userDataObj2 instanceof AbstractGameObject) {
            AbstractGameObject obj1 = (AbstractGameObject) userDataObj1;
            AbstractGameObject obj2 = (AbstractGameObject) userDataObj2;
            if (sceneObjectManager.contains(obj1) && sceneObjectManager.contains(obj2)) {
                players.forEach(player -> {
                    if (obj1 instanceof PlayerUnit && player.isPlayers((PlayerUnit) obj1)) {
                        player.collisionEvent((PlayerUnit) obj1, obj2);
                    }
                    if (obj2 instanceof PlayerUnit && player.isPlayers((PlayerUnit) obj2)) {
                        player.collisionEvent((PlayerUnit) obj2, obj1);
                    }
                });
            }
        }
    }

    public void update() {
        spawnUnits.update(() -> {
            List<PlayerUnit> units = players.stream()
                    .map(player -> player.spawnUnitsOnStartPositions(objectConstructors))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            sceneObjectManager.addAll(units);
        });
        updateUnits.update(() -> players.forEach(Player::update));
        // TODO: 5/5/2020 Optimize to use in parallel, distance calculations
        distanceRecalculate.update(this::calculateDistance);
    }

    private void calculateDistance() {
        List<PlayerUnit> units = players.stream()
                .map(Player::getUnits)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        for (int i = 0; i < units.size(); i++) {
            for (int j = i; j < units.size(); j++) {
                if (i != j) {
                    PlayerUnit unit1 = units.get(i);
                    PlayerUnit unit2 = units.get(j);
                    if (!Objects.equals(unit1.getPlayerName(), unit2.getPlayerName())) {
                        boolean moving1 = unit1.isMoving();
                        boolean moving2 = unit2.isMoving();
                        if (moving1 || moving2) {
                            Vector3 unit1P = unit1.body.getWorldTransform().getTranslation(tempVector1);
                            Vector3 unit2P = unit2.body.getWorldTransform().getTranslation(tempVector2);
                            float dst = unit1P.dst(unit2P);
                            if (moving1) {
                                unit1.unitNear(unit2, dst);
                            }
                            if (moving2) {
                                unit2.unitNear(unit1, dst);
                            }
                        }
                    }
                }
            }
        }
    }

    public long getTimeLeft() {
        return spawnUnits.nextCallIn();
    }

    public long getTotalUnits() {
        return players.stream().map(Player::getUnits).mapToLong(Collection::size).sum();
    }

    @Override
    public void dispose() {
    }

    public static final class Builder {
        private final ObjectConstructors objectConstructors;
        private final SceneObjectManager sceneObjectManager;

        public Builder(ObjectConstructors objectConstructors, SceneObjectManager sceneObjectManager) {
            this.objectConstructors = objectConstructors;
            this.sceneObjectManager = sceneObjectManager;
        }

        public PlayerController build(List<PlayerJson> playerJsons) {
            List<Player> players = playerJsons.stream()
                    .map(playerJson -> {
                        List<List<AbstractGameObject>> paths = playerJson.getPaths().stream()
                                .map(path -> path.stream()
                                        .map(sceneObjectManager::getByModelName)
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList());
                        return new Player(playerJson.getUnitType(), paths, playerJson.getPlayerName());
                    })
                    .collect(Collectors.toList());
            return new PlayerController(players, objectConstructors, sceneObjectManager);
        }
    }

}