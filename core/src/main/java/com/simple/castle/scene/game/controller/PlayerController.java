package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.core.event.CollisionEvent;
import com.simple.castle.core.event.EveryEvent;
import com.simple.castle.core.manager.SceneManager;
import com.simple.castle.core.object.constructors.ObjectConstructors;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.utils.jsondto.PlayerJson;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerController implements CollisionEvent, Disposable {

    public static final long updateUnitsEvery = 100;

    public static final long spawnEvery = 3 * 1000;
    public static final long triggerDistanceEvery = 3 * 1000;
    private static final int TRIGGER_AREA = 20;

    private static final Vector3 tempVector1 = new Vector3();
    private static final Vector3 tempVector2 = new Vector3();

    private final ObjectConstructors objectConstructors;
    private final SceneManager sceneManager;
    private final Set<Player> players;

    private final EveryEvent spawnUnits = new EveryEvent(spawnEvery, true);
    private final EveryEvent distanceRecalculate = new EveryEvent(triggerDistanceEvery, true);
    private final EveryEvent updateUnits = new EveryEvent(updateUnitsEvery, true);

    private PlayerController(Set<Player> players, ObjectConstructors objectConstructors,
                             SceneManager sceneManager) {
        this.objectConstructors = objectConstructors;
        this.sceneManager = sceneManager;
        this.players = players;
    }

    @Override
    public void collisionEvent(AbstractGameObject object1, AbstractGameObject object2) {
        players.forEach(player -> {
            if (object1 instanceof PlayerUnit && player.isPlayers((PlayerUnit) object1)) {
                player.collisionEvent((PlayerUnit) object1, object2);
            }
            if (object2 instanceof PlayerUnit && player.isPlayers((PlayerUnit) object2)) {
                player.collisionEvent((PlayerUnit) object2, object1);
            }
        });
    }

    public void update() {
        spawnUnits.update(() -> {
            List<PlayerUnit> units = players.stream()
                    .map(player -> player.spawnUnitsOnStartPositions(objectConstructors))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            sceneManager.addAll(units);
        });
        updateUnits.update(() -> players.forEach(Player::update));
        // TODO: 5/5/2020 Optimize to use in parallel, distance calculations
        distanceRecalculate.update(this::calculateDistance);

        players.stream()
                .map(Player::getDeadUnitsAndClear)
                .flatMap(Collection::stream)
                .forEach(sceneManager::remove);
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
                        Vector3 unit1P = unit1.body.getWorldTransform().getTranslation(tempVector1);
                        Vector3 unit2P = unit2.body.getWorldTransform().getTranslation(tempVector2);
                        float dst = unit1P.dst(unit2P);
                        if (dst <= TRIGGER_AREA) {
                            unit1.enemyDistanceEvent(unit2, dst);
                            unit2.enemyDistanceEvent(unit1, dst);
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
        private final SceneManager sceneManager;

        public Builder(ObjectConstructors objectConstructors, SceneManager sceneManager) {
            this.objectConstructors = objectConstructors;
            this.sceneManager = sceneManager;
        }

        public PlayerController build(List<PlayerJson> playerJsons) {
            Set<Player> players = playerJsons.stream()
                    .map(playerJson -> {
                        List<List<AbstractGameObject>> paths = playerJson.getPaths().stream()
                                .map(path -> path.stream()
                                        .map(sceneManager::getByModelName)
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList());
                        return new Player(playerJson.getUnitType(), paths, playerJson.getPlayerName());
                    })
                    .collect(Collectors.toSet());
            return new PlayerController(players, objectConstructors, sceneManager);
        }
    }

}