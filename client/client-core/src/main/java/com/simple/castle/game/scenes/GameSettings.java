package com.simple.castle.game.scenes;

import com.badlogic.gdx.utils.Json;
import com.simple.castle.server.game.core.settings.dto.ObjectConstructorsJson;
import com.simple.castle.server.game.core.settings.dto.PlayersJson;
import com.simple.castle.server.game.core.settings.dto.SceneObjectsJson;
import com.simple.castle.server.game.core.settings.util.PropertyLoader;

import java.util.Properties;

public class GameSettings {

    public static final PlayersJson PLAYERS_JSON = new Json()
            .fromJson(PlayersJson.class, PropertyLoader.loadData("/game/players.json"));

    public static final String CAMERA_BASE_PLANE = GAME.getProperty("camera-base-plane");
    public static final String CAMERA_INIT_POSITION_FROM = GAME.getProperty("camera-init-position-from");
    public static final ObjectConstructorsJson OBJECT_CONSTRUCTORS_JSON = new Json()
            .fromJson(ObjectConstructorsJson.class, PropertyLoader.loadData("/game/object-constructors.json"));
    public static final SceneObjectsJson SCENE_OBJECTS_JSON = new Json()
            .fromJson(SceneObjectsJson.class, PropertyLoader.loadData("/game/scene-objects.json"));
    private static final Properties GAME = PropertyLoader.load("/game/scene.properties");

}
