package com.simple.castle.scene.game;

import com.badlogic.gdx.utils.Json;
import com.simple.castle.core.settings.dto.ObjectConstructorsJson;
import com.simple.castle.core.settings.dto.PlayersJson;
import com.simple.castle.core.settings.dto.SceneObjectsJson;
import com.simple.castle.core.settings.util.PropertyLoader;

import java.util.Properties;

public class GameSettings {
    private static final Properties GAME = PropertyLoader.load("/scenes/game/scene.properties");

    public static final String CAMERA_BASE_PLANE = GAME.getProperty("camera-base-plane");
    public static final String CAMERA_INIT_POSITION_FROM = GAME.getProperty("camera-init-position-from");
    public static final PlayersJson PLAYERS_JSON = new Json()
            .fromJson(PlayersJson.class, PropertyLoader.loadData("/scenes/game/players.json"));
    public static final ObjectConstructorsJson OBJECT_CONSTRUCTORS_JSON = new Json()
            .fromJson(ObjectConstructorsJson.class, PropertyLoader.loadData("/scenes/game/object-constructors.json"));
    public static final SceneObjectsJson SCENE_OBJECTS_JSON = new Json()
            .fromJson(SceneObjectsJson.class, PropertyLoader.loadData("/scenes/game/scene-objects.json"));

}
