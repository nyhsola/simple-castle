package com.simple.castle.game.scenes.main.menu;

import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.menu.objects.MenuSceneBackground;
import com.simple.castle.game.scenes.main.menu.objects.MenuSceneMain;

public class MenuScene extends Scene {

    public static final String CAMERA_FIELD_OF_VIEW = "CameraFieldOfView";

    @Override
    public void create() {
        MenuSceneBackground menuSceneBackground = new MenuSceneBackground();
        MenuSceneMain sceneMain = new MenuSceneMain();

        manager
                .addScene(Scenes.MENU_SCENE_BACKGROUND, menuSceneBackground)
                .addScene(Scenes.MENU_SCENE_MENU, sceneMain)
                .setCurrentScene(Scenes.MENU_SCENE_BACKGROUND);
        super.create();
    }

//    @Override
//    public void onChildEvent(Map<String, Object> map) {
//        if (map.containsKey(SceneBaseEvent.TO_SCENE)) {
//            manager.setCurrentScene((String) map.get(SceneBaseEvent.TO_SCENE));
//        }
//        super.onChildEvent(map);
//    }

}
