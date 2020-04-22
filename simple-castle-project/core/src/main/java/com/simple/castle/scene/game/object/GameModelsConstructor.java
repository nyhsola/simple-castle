package com.simple.castle.scene.game.object;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.simple.castle.object.GameObjectConstructor;
import com.simple.castle.utils.GameObjectsUtil;
import com.simple.castle.utils.ModelUtils;
import com.simple.castle.utils.PropertyLoader;
import main.java.com.simple.castle.scene.game.object.GameModel;

import java.util.*;
import java.util.stream.Collectors;

public class GameModelsConstructor {

    private final Model mainModel;
    private final Set<GameModel> gameModels;
    private final Map<String, GameObjectConstructor> constructors;

    public GameModelsConstructor(Model mainModel) {
        this.mainModel = mainModel;
        this.constructors = new HashMap<>();
        this.gameModels = new HashSet<>();

        PropertyLoader.loadGameModels().forEach(gameModelLambda -> {
            GameModel gameModel = new GameModel(gameModelLambda);
            Collection<GameModel> gameModelsAdd = ModelUtils.getNodesFromModelByPattern(mainModel, gameModel.getModel())
                    .stream()
                    .map(modelName -> new GameModel(modelName, gameModel.getShape(), gameModel.getMass()))
                    .collect(Collectors.toList());
            gameModels.addAll(gameModelsAdd);
        });

        constructors.putAll(constructObjects());
    }

    private Map<String, GameObjectConstructor> constructObjects() {
        final BoundingBox tmp = new BoundingBox();
        Map<String, GameObjectConstructor> constructors = new HashMap<>();

        gameModels.forEach((gameModel) -> {
                    btCollisionShape shape = "box".equals(gameModel.getShape())
                            ? GameObjectsUtil.calculateBox(mainModel.getNode(gameModel.getModel()).calculateBoundingBox(tmp))
                            : GameObjectsUtil.calculateSphere(mainModel.getNode(gameModel.getModel()).calculateBoundingBox(tmp));

                    constructors.put(gameModel.getModel(), new GameObjectConstructor(mainModel, gameModel.getModel(), shape, gameModel.getMass()));
                }
        );

        return constructors;
    }

    public void dispose() {
        for (GameObjectConstructor constructor : constructors.values()) {
            constructor.dispose();
        }
        constructors.clear();
    }

    public GameObjectConstructor getConstructor(String name) {
        return constructors.get(name);
    }

}
