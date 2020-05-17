package com.simple.castle.server.game.core.object.constructors;

import com.badlogic.gdx.graphics.g3d.Model;
import com.simple.castle.server.game.core.object.constructors.tool.RigidBodies;
import com.simple.castle.server.game.core.object.unit.add.ObjectConstructor;
import com.simple.castle.server.game.core.settings.dto.ObjectConstructorsJson;
import com.simple.castle.server.game.core.utils.StringTool;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObjectConstructors {

    private final Map<String, ObjectConstructor> constructors = new HashMap<>();

    private ObjectConstructors(Model mainModel, ObjectConstructorsJson objectConstructorsJson) {
        List<ObjectConstructor> collectedConstructors = objectConstructorsJson.getObjectConstructors()
                .stream()
                .map(objectConstructorJson -> fromPattern(objectConstructorJson.getNodesPattern(), mainModel, objectConstructorJson.getMass(), objectConstructorJson.getShape()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        collectedConstructors.forEach(constructor -> constructors.put(constructor.node, constructor));
    }

    private Set<ObjectConstructor> fromPattern(String pattern, Model mainModel, float mass, String shape) {
        Set<String> nodeIds = StreamSupport.stream(mainModel.nodes.spliterator(), false)
                .map(node -> node.id)
                .collect(Collectors.toSet());
        Collection<String> valuesByPattern = StringTool.getValuesByPattern(nodeIds, pattern);
        return valuesByPattern.stream().map(id ->
                new ObjectConstructor(mainModel,
                        id,
                        RigidBodies.valueOf(shape).calculate(mainModel.getNode(id)),
                        mass))
                .collect(Collectors.toSet());
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

        public ObjectConstructors build(ObjectConstructorsJson objectConstructorsJson) {
            return new ObjectConstructors(model, objectConstructorsJson);
        }
    }
}
