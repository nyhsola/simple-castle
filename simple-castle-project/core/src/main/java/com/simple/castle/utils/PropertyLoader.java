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

    private PropertyLoader() {

    }

    public static List<HashMap<String, Object>> loadGameModels() {
        try {
            Properties properties = getProperties();
            List<HashMap<String, Object>> gameModels = new ArrayList<>();
            new JSONObject(CharStreams.toString(
                    new InputStreamReader(
                            PropertyLoader.class.getResourceAsStream("/" + properties.get(MODELS_TO_LOAD)))))
                    .getJSONArray("models")
                    .toList()
                    .forEach(model -> gameModels.add((HashMap<String, Object>) model));
            return gameModels;
        } catch (IOException exception) {
            throw new AssertionError("Missing properties");
        }
    }

    public static List<String> loadGameSceneObjects() {
        try {
            Properties properties = getProperties();
            List<String> gameModels = new ArrayList<>();
            new JSONObject(CharStreams.toString(
                    new InputStreamReader(
                            PropertyLoader.class.getResourceAsStream("/" + properties.get(GAME_SCENE_OBJECTS)))))
                    .getJSONArray("models")
                    .toList()
                    .forEach(model -> gameModels.add((String) ((HashMap<String, Object>) model).get("model")));
            return gameModels;
        } catch (IOException exception) {
            throw new AssertionError("Missing properties");
        }
    }

    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(PropertyLoader.class.getResourceAsStream("/" + APP_PROPERTIES));
        return properties;
    }
}
