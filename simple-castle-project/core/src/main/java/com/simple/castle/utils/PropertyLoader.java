package com.simple.castle.utils;

import com.google.common.io.CharStreams;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public final class PropertyLoader {

    private static final String APP_PROPERTIES = "app.properties";
    private static final String MODELS_TO_LOAD = "models-to-load";
    private static final String GAME_SCENE_OBJECTS = "game-scene-objects";
    private static final String GAME_SCENE_PATH = "game-scene-path";

    private PropertyLoader() {
    }

    @SuppressWarnings("unchecked")
    public static List<HashMap<String, Object>> loadGameModels() {
        List<HashMap<String, Object>> gameModels = new ArrayList<>();
        new JSONObject(PropertyLoader.loadResource((String) getProperties().get(MODELS_TO_LOAD)))
                .getJSONArray("models")
                .toList()
                .forEach(model -> gameModels.add((HashMap<String, Object>) model));
        return gameModels;
    }

    @SuppressWarnings("unchecked")
    public static List<String> loadGameSceneObjects() {
        List<String> gameModels = new ArrayList<>();
        new JSONObject(PropertyLoader.loadResource((String) getProperties().get(GAME_SCENE_OBJECTS)))
                .getJSONArray("models")
                .toList()
                .forEach(model -> gameModels.add((String) ((HashMap<String, Object>) model).get("model")));
        return gameModels;
    }

    public static String loadGameScenePath() {
        return PropertyLoader.loadResource((String) getProperties().get(GAME_SCENE_PATH));
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(PropertyLoader.class.getResourceAsStream("/" + APP_PROPERTIES));
            return properties;
        } catch (IOException exception) {
            throw new AssertionError("Missing file", exception);
        }
    }

    private static String loadResource(String resource) {
        try {
            return CharStreams.toString(
                    new InputStreamReader(
                            PropertyLoader.class.getResourceAsStream("/" + resource)));
        } catch (IOException exception) {
            throw new AssertionError("Missing file", exception);
        }
    }
}
