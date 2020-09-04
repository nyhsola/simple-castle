package com.simple.castle.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;

public class Demo {
    public static void main(String[] args) {
        new LwjglApplication(new ApplicationListener() {
            private PerspectiveCamera camera;
            private ModelBatch modelBatch;
            private Model model;
            private ModelInstance modelInstance;
            private Environment environment;
            private AnimationController controller;

            @Override
            public void create() {
                // Create camera sized to screens width/height with Field of View of 75 degrees
                camera = new PerspectiveCamera(
                        75,
                        Gdx.graphics.getWidth(),
                        Gdx.graphics.getHeight());

                // Move the camera 5 units back along the z-axis and look at the origin
                camera.position.set(0f, 0f, 7f);
                camera.lookAt(0f, 0f, 0f);

                // Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
                camera.near = 0.1f;
                camera.far = 300.0f;
                // A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
                modelBatch = new ModelBatch();

                // Model loader needs a binary json reader to decode
                UBJsonReader jsonReader = new UBJsonReader();
                // Create a model loader passing in our json reader
                G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
                // Now load the model by name
                // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
                model = modelLoader.loadModel(Gdx.files.internal("models/map.g3dj"));
                // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
                modelInstance = new ModelInstance(model, "Armature", "animtest");

                //fbx-conv is supposed to perform this rotation for you... it doesnt seem to
                modelInstance.transform.rotate(1, 1, 0, -90);
                //move the model down a bit on the screen ( in a z-up world, down is -z ).
                modelInstance.transform.translate(0, 0, -2);
                // Finally we want some light, or we wont see our color.  The environment gets passed in during
                // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
                environment = new Environment();
                environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

                // You use an AnimationController to um, control animations.  Each control is tied to the model instance
                controller = new AnimationController(modelInstance);
                // Pick the current animation by name
                controller.setAnimation("Armature|ArmatureAction", 1, new AnimationController.AnimationListener() {
                    @Override
                    public void onEnd(AnimationController.AnimationDesc animation) {
                        // this will be called when the current animation is done.
                        // queue up another animation called "balloon".
                        // Passing a negative to loop count loops forever.  1f for speed is normal speed.
//                        controller.queue("balloon", -1, 1f, null, 0f);
                    }

                    @Override
                    public void onLoop(AnimationController.AnimationDesc animation) {
                        // TODO Auto-generated method stub
                    }

                });
            }

            @Override
            public void dispose() {
                modelBatch.dispose();
                model.dispose();
            }

            @Override
            public void render() {
                // You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT when working in 3D
                Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

                // For some flavor, lets spin our camera around the Y axis by 1 degree each time render is called
                //camera.rotateAround(Vector3.Zero, new Vector3(0,1,0),1f);
                // When you change the camera details, you need to call update();
                // Also note, you need to call update() at least once.
                camera.update();

                // You need to call update on the animation controller so it will advance the animation.  Pass in frame delta
                controller.update(Gdx.graphics.getDeltaTime());
                // Like spriteBatch, just with models!  pass in the box Instance and the environment
                modelBatch.begin(camera);
                modelBatch.render(modelInstance, environment);
                modelBatch.end();
            }

            @Override
            public void resize(int width, int height) {
            }

            @Override
            public void pause() {
            }

            @Override
            public void resume() {
            }
        });
    }
}
