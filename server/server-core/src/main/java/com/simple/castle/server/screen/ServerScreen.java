package com.simple.castle.server.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.base.World;
import com.simple.castle.base.asset.AssetLoader;
import com.simple.castle.server.composition.BaseObject;
import com.simple.castle.server.composition.Constructor;
import com.simple.castle.server.composition.add.InteractType;
import com.simple.castle.server.composition.add.PhysicShape;
import com.simple.castle.server.physic.world.PhysicWorld;
import com.simple.castle.server.render.BaseCamera;
import com.simple.castle.server.render.BaseEnvironment;
import com.simple.castle.server.render.BaseRenderer;
import com.simple.castle.server.tcp.DataListener;

import java.util.ArrayList;
import java.util.List;

public class ServerScreen extends ScreenAdapter implements InputProcessor {

    private final BaseRenderer baseRenderer;
    private final BaseEnvironment baseEnvironment;
    private final BaseCamera baseCamera;

    private final InputMultiplexer inputMultiplexer;

    private final PhysicWorld physicWorld;
    private final List<BaseObject> baseObjects;

    private final List<DataListener> dataListeners;

    private final Filler filler = new Filler();
    private final World world = new World();

    public ServerScreen(BaseRenderer baseRenderer) {
        Model model = new AssetLoader().loadModel();
        BaseObject baseObject = new BaseObject(new Constructor(model, "ground", InteractType.KINEMATIC, PhysicShape.STATIC));
        BaseObject baseObject1 = new BaseObject(new Constructor(model, "unit-type-1", InteractType.KINEMATIC, PhysicShape.STATIC));

        this.baseRenderer = baseRenderer;
        this.baseEnvironment = new BaseEnvironment();
        this.baseCamera = new BaseCamera(baseObject1.getModelInstance().transform.getTranslation(new Vector3()));

        this.dataListeners = new ArrayList<>();

        physicWorld = new PhysicWorld();
        physicWorld.addRigidBody(baseObject.getPhysicObject());

        this.baseObjects = new ArrayList<>();
        this.baseObjects.add(baseObject);
        this.baseObjects.add(baseObject1);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(baseCamera);
    }

    @Override
    public void render(float delta) {
        baseCamera.update(delta);
        physicWorld.update(delta);
        baseRenderer.render(baseCamera, baseObjects, baseEnvironment);
        for (DataListener dataListener : dataListeners) {
            dataListener.worldTick(world);
        }
    }

    @Override
    public void resize(int width, int height) {
        baseCamera.resize(width, height);
        baseCamera.update();
    }

    @Override
    public void dispose() {
        baseRenderer.dispose();
        physicWorld.dispose();
        baseObjects.forEach(BaseObject::dispose);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit();
        }
        return inputMultiplexer.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputMultiplexer.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return inputMultiplexer.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputMultiplexer.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return inputMultiplexer.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inputMultiplexer.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return inputMultiplexer.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return inputMultiplexer.scrolled(amount);
    }

    public void addDataListener(DataListener dataListener) {
        this.dataListeners.add(dataListener);
    }
}
