package com.simple.castle.utils;

import com.google.common.io.CharStreams;
import org.json.JSONArray;
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

    private PropertyLoader() {

    }

    public static List<HashMap<String, Object>> loadGameModels() {
        try {
            Properties properties = new Properties();
            properties.load(PropertyLoader.class.getResourceAsStream("/" + APP_PROPERTIES));
            String name = "/" + properties.get(MODELS_TO_LOAD);
            String content = CharStreams.toString(new InputStreamReader(PropertyLoader.class.getResourceAsStream(name)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray models = jsonObject.getJSONArray("models");
            List<HashMap<String, Object>> gameModels = new ArrayList<>();
            models.toList().forEach(model -> gameModels.add((HashMap<String, Object>) model));
            return gameModels;
        } catch (IOException exception) {
            throw new AssertionError("Missing properties");
        }
    }
}
