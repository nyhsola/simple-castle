package com.simple.castle.utils;

import com.badlogic.gdx.graphics.g3d.Model;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class ModelUtils {
    private ModelUtils() {

    }

    public static Collection<String> getNodesFromModelByPattern(Model model, String pattern) {
        return getValuesByPattern(
                StreamSupport.stream(model.nodes.spliterator(), false)
                        .map(node -> node.id)
                        .collect(Collectors.toSet()), pattern);
    }

    public static Collection<String> getValuesByPattern(Set<String> modelNames, String pattern) {
        return modelNames.stream()
                .filter(nodes -> nodes.matches(pattern))
                .collect(Collectors.toList());
    }
}
