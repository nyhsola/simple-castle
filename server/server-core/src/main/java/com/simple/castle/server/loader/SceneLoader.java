package com.simple.castle.server.loader;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Json;
import com.simple.castle.server.json.SceneObjectJson;
import com.simple.castle.server.json.SceneObjectsJson;
import com.simple.castle.server.kt.game.ServerGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class SceneLoader {
    private static final Json JSON = new Json();

    private SceneLoader() {
    }

    private static String loadData(String path) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(ServerGame.class.getResourceAsStream(path), StandardCharsets.UTF_8))) {
                return br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException exception) {
            throw new AssertionError("Missing such props", exception);
        }
    }

    private static Collection<String> getValuesByPattern(Set<String> modelNames, String pattern) {
        return modelNames.stream()
                .filter(nodes -> nodes.matches(pattern))
                .collect(Collectors.toList());
    }

    public static SceneObjectsJson loadSceneObjects(Model model) {
        Set<String> nodeNames = extractAllNodeNames(model);
        SceneObjectsJson sceneObjectsJson = JSON.fromJson(SceneObjectsJson.class, loadData("/game-scene-objects.json"));

        List<SceneObjectJson> extracted = sceneObjectsJson.getSceneObjectsJson().stream()
                .map(extractImplicitNodeNames(nodeNames))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        sceneObjectsJson.setSceneObjectsJson(extracted);
        return sceneObjectsJson;
    }

    private static Function<SceneObjectJson, List<SceneObjectJson>> extractImplicitNodeNames(Set<String> nodeNames) {
        return sceneObjectJson -> {
            Collection<String> nodes = getValuesByPattern(nodeNames, sceneObjectJson.getNodePattern());
            return nodes.stream().map(implicitNode -> {
                SceneObjectJson sceneObjectJsonNew = new SceneObjectJson(sceneObjectJson);
                sceneObjectJsonNew.setNodePattern(implicitNode);
                return sceneObjectJsonNew;
            }).collect(Collectors.toList());
        };
    }

    private static Set<String> extractAllNodeNames(Model model) {
        return StreamSupport.stream(model.nodes.spliterator(), false)
                .map(node -> node.id)
                .collect(Collectors.toSet());
    }
}
