package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.listener.SceneObjectManager;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.constructors.SceneObjectsHandler;
import com.simple.castle.object.unit.BasicUnit;
import com.simple.castle.object.unit.abs.AbstractGameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerController implements CollisionEvent {

    private static final List<String> redLeftPath = Arrays.asList(
            "area-1-1", "area-1-1-1", "area-2-3", "area-2",
            "area-2-1", "area-2-1-1", "area-3-3", "area-3");
    private static final List<String> redMiddlePath = Arrays.asList(
            "area-1-2", "area-0", "area-3-2", "area-3"
    );
    private static final List<String> redRightPath = Arrays.asList(
            "area-1-3", "area-4-1-1", "area-4-1", "area-4",
            "area-4-3", "area-3-1-1", "area-3-1", "area-3"
    );
    private static final List<String> blueRightPath = Arrays.asList(
            "area-2-3", "area-1-1-1", "area-1-1", "area-1",
            "area-1-3", "area-4-1-1", "area-4-1", "area-4"
    );

    public static final long spawnEvery = 3 * 1000;
    private final ObjectConstructors objectConstructors;
    private final SceneObjectManager sceneObjectManager;

    private final List<Player> players;
    private long timeLeft = spawnEvery;
    private long previousTime = System.currentTimeMillis();
    private final SceneObjectsHandler sceneObjectsHandler;

    public PlayerController(ObjectConstructors objectConstructors, SceneObjectsHandler sceneObjectsHandler,
                            SceneObjectManager sceneObjectManager) {
        this.objectConstructors = objectConstructors;
        this.sceneObjectsHandler = sceneObjectsHandler;
        this.sceneObjectManager = sceneObjectManager;

        // TODO: 5/5/2020 Move SceneObjectsHandler to GameScene, unit-types and paths load from file
        players = new ArrayList<>();
        players.add(new Player("unit-type-1",
                toAbsList(sceneObjectsHandler, Stream.of(redLeftPath, redMiddlePath, redRightPath))));
        players.add(new Player("unit-type-2",
                toAbsList(sceneObjectsHandler, Stream.of(blueRightPath))));
    }

    private static List<List<AbstractGameObject>> toAbsList(SceneObjectsHandler sceneObjectsHandler,
                                                            Stream<List<String>> paths) {
        return paths.map(pathsString -> pathsString.stream()
                .map(sceneObjectsHandler::getSceneObject)
                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public void collisionEvent(btCollisionObject object1, btCollisionObject object2) {
        Object userDataObj1 = object1.userData;
        Object userDataObj2 = object2.userData;
        if (userDataObj1 instanceof String && userDataObj2 instanceof String) {
            String userData1 = (String) userDataObj1;
            String userData2 = (String) userDataObj2;
            players.forEach(player -> {
                if (player.isPlayers(userData1) && sceneObjectsHandler.contains(userData2)) {
                    player.collisionEvent(
                            (BasicUnit) sceneObjectsHandler.getSceneObject(userData1),
                            sceneObjectsHandler.getSceneObject(userData2));
                }
                if (player.isPlayers(userData2) && sceneObjectsHandler.contains(userData1)) {
                    player.collisionEvent(
                            (BasicUnit) sceneObjectsHandler.getSceneObject(userData2),
                            sceneObjectsHandler.getSceneObject(userData1));
                }
            });
        }
    }

    public void update() {
        long diff = System.currentTimeMillis() - previousTime;
        timeLeft = timeLeft - diff;

        if (timeLeft <= 0) {
            timeLeft = spawnEvery;
            List<BasicUnit> basicUnits = this.spawnUnits(objectConstructors);
            basicUnits.forEach(sceneObjectManager::add);
        }
        previousTime = System.currentTimeMillis();

        // TODO: 5/5/2020 Check distances between objects
        players.forEach(Player::update);
    }

    private List<BasicUnit> spawnUnits(ObjectConstructors objectConstructors) {
        return players.stream()
                .map(player -> player.spawnUnitsOnStartPositions(objectConstructors))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}