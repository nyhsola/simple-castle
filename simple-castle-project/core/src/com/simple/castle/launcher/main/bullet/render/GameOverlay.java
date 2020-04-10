package com.simple.castle.launcher.main.bullet.render;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.launcher.main.bullet.object.GameObject;

import java.util.Locale;

public class GameOverlay extends ApplicationAdapter {

    private BitmapFont bitmapFont;
    private SpriteBatch batch;

    private Vector3 tmpVector3 = new Vector3();
    private Quaternion tmpQuaternion = new Quaternion();

    @Override
    public void create() {
        bitmapFont = new BitmapFont();
        batch = new SpriteBatch();
    }

    public void render(GameCamera gameCamera, GameObject selected) {
        batch.begin();

        bitmapFont.draw(batch, "Camera position: " +
                format(gameCamera.position), 0, 20);

        if (selected != null) {
            bitmapFont.draw(batch, "Selected (model): " +
                    "Position: " + format(selected.transform.getTranslation(tmpVector3)) + " " +
                    "Rotation: " + format(selected.transform.getRotation(tmpQuaternion)), 0, 40);

            bitmapFont.draw(batch, "Selected (physic): " +
                    "Position: " + format(selected.body.getWorldTransform().getTranslation(tmpVector3)) + " " +
                    "Rotation: " + format(selected.body.getWorldTransform().getRotation(tmpQuaternion)), 0, 60);

            bitmapFont.draw(batch, selected.node, 0, 80);
        }

        batch.end();
    }

    private String format(Vector3 vector3) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f", vector3.x, vector3.y, vector3.z);
    }

    private String format(Quaternion quaternion) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f, %.2f", quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }
}
