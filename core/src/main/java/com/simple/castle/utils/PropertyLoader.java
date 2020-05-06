package com.simple.castle.utils;

import com.google.common.io.CharStreams;
import com.simple.castle.utils.jsondto.PlayersJson;
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

    public static List<Map<String, Object>> loadObjects(String fromScene) {
        return loadListMap(SCENES_FOLDER + findScene(fromScene) + "/scene-objects.json",
                "models");
    }

    public static List<PlayersJson> loadPlayers(String fromScene) {
        return loadListMap(SCENES_FOLDER + findScene(fromScene) + "/players.json", "players")
                .stream()
                .map(listMap -> {
                    PlayersJson playersJson = new PlayersJson();
                    playersJson.setPlayerName((String) listMap.get("player-name"));
                    playersJson.setUnitType((String) listMap.get("unit-type"));
                    List<List<String>> paths = castToList(listMap.get("paths"))
                            .stream()
                            .map(PropertyLoader::castToMap)
                            .map(map -> map.get("path"))
                            .map(path -> (String) path)
                            .map(pathString -> Arrays.asList(pathString.split(" ")))
                            .collect(Collectors.toList());
                    playersJson.setPaths(paths);
                    return playersJson;
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
