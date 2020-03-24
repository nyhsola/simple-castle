package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.JsonReader;

public class GameLauncher extends ApplicationAdapter {
    private static final String MODELS_PLANE_G_3_DJ = "models/surface.g3dj";
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private ModelBatch modelBatch;
    private Model model;
    private Environment environment;

    private ModelInstance surface;
    private ModelInstance redCube;
    private ModelInstance sphere;
    private ModelInstance sphereBox;

    private btCollisionObject surfaceObject;
    private btCollisionObject redCubeObject;

    private GameCamera gameCamera;
    private InputMultiplexer input;

    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;

    public static btConvexHullShape createConvexHullShape(final Model model, boolean optimize) {
        final Mesh mesh = model.meshes.get(0);
        final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
        if (!optimize) return shape;
        // now optimize the shape
        final btShapeHull hull = new btShapeHull(shape);
        hull.buildHull(shape.getMargin());
        final btConvexHullShape result = new btConvexHullShape(hull);
        // delete the temporary shape
        shape.dispose();
        hull.dispose();
        return result;
    }

    @Override
    public void create() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        model = modelLoader.loadModel(Gdx.files.internal(MODELS_PLANE_G_3_DJ));

        surface = new ModelInstance(model, "Surface");
        redCube = new ModelInstance(model, "RedCube");
        sphere = new ModelInstance(model, "Sphere");

        BoundingBox out = new BoundingBox();
        sphere.calculateBoundingBox(out);
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        BoxShapeBuilder.build(modelBuilder.part("id", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material()), out);
        sphereBox = new ModelInstance(modelBuilder.end());

        gameCamera = new GameCamera(surface, redCube.getNode("RedCube").translation);
        gameCamera.create();

        input = new InputMultiplexer();
        input.addProcessor(gameCamera);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(gameCamera.getCamera());
        modelBatch.render(surface, environment);
        modelBatch.render(redCube, environment);
        modelBatch.render(sphere, environment);
        modelBatch.render(sphereBox);
        modelBatch.end();

//        if(checkCollision()) {
//            redCube.transform.translate(new Vector3(0, 0.01f, 0));
//            redCube.calculateTransforms();
//            redCubeObject.setWorldTransform(redCube.transform);
//
//            Vector3 position;
//            position = redCube.transform.getTranslation(new Vector3());
//
//            Gdx.app.log("tag", "Collision " + position);
//        }
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
        surfaceObject.dispose();
        redCubeObject.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();
    }

    boolean checkCollision() {
        CollisionObjectWrapper surfaceWrapper = new CollisionObjectWrapper(surfaceObject);
        CollisionObjectWrapper cubeWrapper = new CollisionObjectWrapper(redCubeObject);

        btCollisionAlgorithmConstructionInfo constructionInfo = new btCollisionAlgorithmConstructionInfo();
        constructionInfo.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, constructionInfo, surfaceWrapper.wrapper, cubeWrapper.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(surfaceWrapper.wrapper, cubeWrapper.wrapper);

        algorithm.processCollision(surfaceWrapper.wrapper, cubeWrapper.wrapper, info, result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        result.dispose();
        info.dispose();
        algorithm.dispose();
        constructionInfo.dispose();
        cubeWrapper.dispose();
        surfaceWrapper.dispose();

        return r;
    }

//    private FloatBuffer getFloatBuffer(Node model) {
//        MeshPart meshPart = model.parts.get(0).meshPart;
//
//        float[] nVerts = new float[meshPart.size];
//        meshPart.mesh.getVertices(nVerts);
//
//        ByteBuffer allocate = ByteBuffer.allocateDirect(nVerts.length * 4);
//        allocate.order(ByteOrder.nativeOrder());
//
//        FloatBuffer floatBuffer1 = allocate.asFloatBuffer();
//        floatBuffer1.put(nVerts);
//        floatBuffer1.position(0);
//
//        return floatBuffer1;
//    }
}
