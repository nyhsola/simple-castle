package com.simple.castle.utils;

import com.google.common.io.CharStreams;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public final class PropertyLoader {

    private static final String APP_PROPERTIES = "app.properties";

    private PropertyLoader() {
    }

    public static List<HashMap<String, Object>> loadConstructorsFromScene(String sceneNameSearch) {
        List<HashMap<String, Object>> gameModels = new ArrayList<>();

        String sceneName = findScene(sceneNameSearch);

        new JSONObject(loadResource("scenes/" + sceneName + "/scene-object-constructors.json"))
                .getJSONArray("models")
                .toList()
                .forEach(model -> gameModels.add(castToMap(model)));
        return gameModels;
    }

    public static List<String> loadObjectsFromScene(String sceneNameSearch) {
        List<String> sceneObjects = new ArrayList<>();

        String sceneName = findScene(sceneNameSearch);

        new JSONObject(loadResource("scenes/" + sceneName + "/scene-objects.json"))
                .getJSONArray("models")
                .toList()
                .forEach(model -> sceneObjects.add((String) castToMap(model).get("model")));

        return sceneObjects;
    }

    public static Properties loadPropertiesFromScene(String sceneNameSearch) {
        String sceneName = findScene(sceneNameSearch);
        return loadProperties("scenes/" + sceneName + "/scene.properties");
    }

    private static String findScene(String sceneName) {
        String scenesFile = (String) loadProperties(APP_PROPERTIES).get("scenes-file");
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

    private static Properties loadProperties(String resource) {
        Properties properties = new Properties();
        try {
            properties.load(loadInputStream(resource));
            return properties;
        } catch (IOException exception) {
            throw new AssertionError("Missing file", exception);
        }
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
    private static HashMap<String, Object> castToMap(Object object) {
        return ((HashMap<String, Object>) object);
    }
}
