package com.simple.castle.utils;

import com.badlogic.gdx.graphics.g3d.Model;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class ModelUtils {
    private ModelUtils() {

    }

    public static Collection<String> getNodesFromModelByPattern(Model model, String pattern) {
        return StreamSupport.stream(model.nodes.spliterator(), false)
                .filter(nodes -> nodes.id.matches(pattern))
                .map(node -> node.id)
                .collect(Collectors.toList());
    }
}
