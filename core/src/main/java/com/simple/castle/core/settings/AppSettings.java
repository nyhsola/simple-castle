package com.simple.castle.core.settings;

import com.badlogic.gdx.utils.Json;
import com.simple.castle.core.settings.dto.ScenesJson;
import com.simple.castle.core.settings.util.PropertyLoader;

import java.util.Properties;

public class AppSettings {
    private static final Properties APP = PropertyLoader.load("/app.properties");
    private static final String SCENES_JSON_FILE = APP.getProperty("scenes-file");

    public static final ScenesJson SCENES_JSON = new Json().fromJson(ScenesJson.class, PropertyLoader.loadData(SCENES_JSON_FILE));
}
