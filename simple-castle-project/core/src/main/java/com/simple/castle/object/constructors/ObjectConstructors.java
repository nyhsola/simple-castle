package com.simple.castle.object.constructors;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.simple.castle.utils.ModelUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectConstructors {

    private final Map<String, ObjectConstructor> constructors = new HashMap<>();

    private ObjectConstructors(Model mainModel, List<Map<String, Object>> constructorsPattern) {
        List<ObjectConstructor> collectedConstructors = constructorsPattern
                .stream()
                .map(loadNodesFromModelByPattern(mainModel))
                .flatMap(Collection::stream)
                .map((gameModelJson) -> {
                            btCollisionShape shape = RigidBodies.valueOf(gameModelJson.getShape())
                                    .calculate(mainModel.getNode(gameModelJson.getNodesPattern()));
                            return new ObjectConstructor(mainModel, gameModelJson.getNodesPattern(), shape, gameModelJson.getMass());
                        }
                )
                .collect(Collectors.toList());

        collectedConstructors.forEach(constructor -> constructors.put(constructor.node, constructor));
    }

    private Function<Map<String, Object>, List<GameModelJson>> loadNodesFromModelByPattern(Model mainModel) {
        return map -> {
            GameModelJson dto = new GameModelJson(map);
            return ModelUtils.getNodesFromModelByPattern(mainModel, dto.getNodesPattern())
                    .stream()
                    .map(modelName -> new GameModelJson(modelName, dto.getShape(), dto.getMass(), dto.getInteract()))
                    .collect(Collectors.toList());
        };
    }

    public void dispose() {
        for (ObjectConstructor constructor : constructors.values()) {
            constructor.dispose();
        }
        constructors.clear();
    }

    public ObjectConstructor getConstructor(String name) {
        return constructors.get(name);
    }

    public Set<String> getAllConstructors() {
        return constructors.keySet();
    }

    public static final class Builder {
        private final Model model;

        public Builder(Model model) {
            this.model = model;
        }

        public ObjectConstructors build(List<Map<String, Object>> constructorsPattern) {
            return new ObjectConstructors(model, constructorsPattern);
        }
    }
}
