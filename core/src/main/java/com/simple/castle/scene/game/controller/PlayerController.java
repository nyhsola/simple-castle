package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.listener.SceneObjectManager;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.unit.BasicUnit;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.utils.jsondto.PlayerJson;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class PlayerController implements CollisionEvent, Disposable {

    public static final long spawnEvery = 3 * 1000;

    private static final Vector3 tempVector1 = new Vector3();
    private static final Vector3 tempVector2 = new Vector3();

    private final ObjectConstructors objectConstructors;
    private final SceneObjectManager sceneObjectManager;
    private final List<Player> players;

    private final Timer timer;
    private final SpawnerTask spawnerTask;

    private PlayerController(List<Player> players, ObjectConstructors objectConstructors,
                             SceneObjectManager sceneObjectManager) {
        this.objectConstructors = objectConstructors;
        this.sceneObjectManager = sceneObjectManager;
        this.players = players;

        this.spawnerTask = new SpawnerTask();

        this.timer = new Timer(true);
        this.timer.scheduleAtFixedRate(spawnerTask, 0, spawnEvery);
    }

    @Override
    public void collisionEvent(btCollisionObject object1, btCollisionObject object2) {
        Object userDataObj1 = object1.userData;
        Object userDataObj2 = object2.userData;
        if (userDataObj1 instanceof String && userDataObj2 instanceof String) {
            String userData1 = (String) userDataObj1;
            String userData2 = (String) userDataObj2;
            if (sceneObjectManager.contains(userData1) && sceneObjectManager.contains(userData2)) {
                AbstractGameObject sceneObj1 = sceneObjectManager.getByUserData(userData1);
                AbstractGameObject sceneObj2 = sceneObjectManager.getByUserData(userData2);
                players.forEach(player -> {
                    if (player.isPlayers(sceneObj1)) {
                        player.collisionEvent((BasicUnit) sceneObj1, sceneObj2);
                    }
                    if (player.isPlayers(sceneObj2)) {
                        player.collisionEvent((BasicUnit) sceneObj2, sceneObj1);
                    }
                });
            }
        }
    }

    public void update() {
        spawnerTask.getAndClearSpawnedUnits().forEach(sceneObjectManager::add);
        players.forEach(Player::update);

        // TODO: 5/5/2020 Optimize to use in parallel, distance calculations
//        List<BasicUnit> units = players.stream()
//                .map(Player::getUnits)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
//        for (int i = 0; i < units.size(); i++) {
//            for (int j = 0; j < units.size(); j++) {
//                if (i != j) {
//                    BasicUnit unit1 = units.get(i);
//                    BasicUnit unit2 = units.get(j);
//
//                    Vector3 unit1P = unit1.body.getWorldTransform().getTranslation(tempVector1);
//                    Vector3 unit2P = unit2.body.getWorldTransform().getTranslation(tempVector2);
//
//                    Player playerWhoseUnitsSameColor = players.stream()
//                            .filter(player -> player.isPlayers(unit1) && player.isPlayers(unit2))
//                            .findAny()
//                            .orElse(null);
//
//                    float dst = unit1P.dst(unit2P);
//                    if (playerWhoseUnitsSameColor == null) {
//                        unit1.unitNear(unit2, dst);
//                        unit2.unitNear(unit1, dst);
//                    }
//                }
//            }
//        }
    }

    private List<BasicUnit> spawnUnits() {
        return players.stream()
                .map(player -> player.spawnUnitsOnStartPositions(objectConstructors))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public long getTimeLeft() {
        return spawnEvery - (System.currentTimeMillis() - spawnerTask.previousTime);
    }

    @Override
    public void dispose() {
        timer.cancel();
        timer.purge();
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
                                        .map(sceneObjectManager::getByName)
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList());
                        return new Player(playerJson.getUnitType(), paths);
                    })
                    .collect(Collectors.toList());
            return new PlayerController(players, objectConstructors, sceneObjectManager);
        }
    }

    private final class SpawnerTask extends TimerTask {

        private final ReentrantLock lock = new ReentrantLock();
        private List<BasicUnit> basicUnits = new ArrayList<>();
        private long previousTime = System.currentTimeMillis();

        @Override
        public void run() {
            lock.lock();
            try {
                basicUnits.addAll(PlayerController.this.spawnUnits());
            } finally {
                lock.unlock();
            }
            previousTime = System.currentTimeMillis();
        }

        public List<BasicUnit> getAndClearSpawnedUnits() {
            if (basicUnits.isEmpty()) {
                return Collections.emptyList();
            }
            lock.lock();
            List<BasicUnit> ref;
            try {
                ref = basicUnits;
                basicUnits = new ArrayList<>();
            } finally {
                lock.unlock();
            }
            return ref;
        }
    }
}