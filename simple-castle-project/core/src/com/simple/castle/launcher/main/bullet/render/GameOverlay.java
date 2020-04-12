package com.simple.castle.launcher.main.bullet.render;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.launcher.main.bullet.object.AbstractGameObject;

import java.util.Locale;

public class GameOverlay extends ApplicationAdapter {

    private final Quaternion tmpQuaternion = new Quaternion();
    private final Vector3 tmpVector3 = new Vector3();

    private BitmapFont bitmapFont;
    private SpriteBatch batch;

    @Override
    public void create() {
        bitmapFont = new BitmapFont();
        batch = new SpriteBatch();
    }

    public void render(GameCamera gameCamera, AbstractGameObject selected) {
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

    @Override
    public void dispose() {
        bitmapFont.dispose();
        batch.dispose();
    }

    private String format(Vector3 vector3) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f", vector3.x, vector3.y, vector3.z);
    }

    private String format(Quaternion quaternion) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f, %.2f", quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }
}
