package com.simple.castle.core.object.constructors;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.simple.castle.core.object.constructors.tool.RigidBodies;
import com.simple.castle.core.object.unit.add.ObjectConstructor;
import com.simple.castle.core.utils.StringTool;
import com.simple.castle.core.utils.jsondto.GameModelJson;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    private Function<Map<String, Object>, List<com.simple.castle.core.utils.jsondto.GameModelJson>> loadNodesFromModelByPattern(Model mainModel) {
        return map -> {
            com.simple.castle.core.utils.jsondto.GameModelJson dto = new com.simple.castle.core.utils.jsondto.GameModelJson(map);
            return StringTool.getValuesByPattern(
                    StreamSupport.stream(mainModel.nodes.spliterator(), false)
                            .map(node -> node.id)
                            .collect(Collectors.toSet()), dto.getNodesPattern())
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
