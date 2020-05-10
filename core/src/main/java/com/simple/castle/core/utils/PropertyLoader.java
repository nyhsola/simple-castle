package com.simple.castle.core.utils;

import com.google.common.io.CharStreams;
import com.simple.castle.core.utils.jsondto.PlayerJson;
import com.simple.castle.core.utils.jsondto.SceneObjectJson;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public final class PropertyLoader {

    // TODO: 5/7/2020 JSON from the box of libgdx
    private static final String SCENES_FOLDER = "scenes/";
    private static final String APP_PROPERTIES = "app.properties";

    private PropertyLoader() {
    }

    public static Properties loadProperties(String fromScene) {
        return getProperties(SCENES_FOLDER + findScene(fromScene) + "/scene.properties");
    }

    public static List<Map<String, Object>> loadConstructors(String fromScene) {
        return loadListMap(SCENES_FOLDER + findScene(fromScene) + "/scene-object-constructors.json",
                "models");
    }

    public static List<com.simple.castle.core.utils.jsondto.SceneObjectJson> loadObjects(String fromScene) {
        return loadListMap(SCENES_FOLDER + findScene(fromScene) + "/scene-objects.json", "scene-objects")
                .stream()
                .map(listMap -> {
                    com.simple.castle.core.utils.jsondto.SceneObjectJson sceneObjectJson = new SceneObjectJson();
                    sceneObjectJson.setModel((String) listMap.get("model"));
                    sceneObjectJson.setInteract((String) listMap.get("interact"));
                    sceneObjectJson.setHide(String.valueOf(listMap.get("hide")));
                    return sceneObjectJson;
                })
                .collect(Collectors.toList());
    }

    public static List<com.simple.castle.core.utils.jsondto.PlayerJson> loadPlayers(String fromScene) {
        return loadListMap(SCENES_FOLDER + findScene(fromScene) + "/players.json", "players")
                .stream()
                .map(listMap -> {
                    com.simple.castle.core.utils.jsondto.PlayerJson playerJson = new PlayerJson();
                    playerJson.setPlayerName((String) listMap.get("player-name"));
                    playerJson.setUnitType((String) listMap.get("unit-type"));
                    List<List<String>> paths = castToList(listMap.get("paths"))
                            .stream()
                            .map(PropertyLoader::castToMap)
                            .map(map -> map.get("path"))
                            .map(path -> (String) path)
                            .map(pathString -> Arrays.asList(pathString.split(" ")))
                            .collect(Collectors.toList());
                    playerJson.setPaths(paths);
                    return playerJson;
                })
                .collect(Collectors.toList());
    }

    private static String findScene(String sceneName) {
        String scenesFile = (String) getProperties(APP_PROPERTIES).get("scenes-file");
        return (String) new JSONObject(loadResource(scenesFile))
                .getJSONArray("scenes")
                .toList()
                .stream()
                .filter(scene -> scene.equals(sceneName))
                .collect(Collectors.toSet())
                .stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing scene properties"));
    }

    private static Properties getProperties(String resource) {
        Properties properties = new Properties();
        try {
            properties.load(loadInputStream(resource));
            return properties;
        } catch (IOException exception) {
            throw new AssertionError("Missing file", exception);
        }
    }

    private static List<Map<String, Object>> loadListMap(String jsonFile, String array) {
        return new JSONObject(loadResource(jsonFile))
                .getJSONArray(array)
                .toList()
                .stream()
                .map(PropertyLoader::castToMap)
                .collect(Collectors.toList());
    }

    private static String loadResource(String resource) {
        try {
            return CharStreams.toString(new InputStreamReader(loadInputStream(resource)));
        } catch (IOException exception) {
            throw new AssertionError("Missing file", exception);
        }
    }

    private static InputStream loadInputStream(String resource) {
        return PropertyLoader.class.getResourceAsStream("/" + resource);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castToMap(Object object) {
        return ((Map<String, Object>) object);
    }

    @SuppressWarnings("unchecked")
    private static List<Object> castToList(Object object) {
        return ((List<Object>) object);
    }
}
