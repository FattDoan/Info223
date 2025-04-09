package processing;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PImage;
import processing.opengl.PShader;
import processing.core.PShape;

public class Main extends PApplet {
    public static void main(String[] args) {
        //String[] processingArgs = {"Main"};
        //Main mySketch = new Main();
        //PApplet.runSketch(processingArgs, mySketch);
        PApplet.main("processing.Main");
    }

    Configs.ConfigsReader reader = Configs.getReader();
    Configs.ConfigsWriter writer = Configs.getWriter(); 
    Camera camera;
    PImage textures;
    ShapeFactory sF;
    Pyramid pyramid;
    PyramidExterior pyEx;
    Frustum frustum;
    CollisionDetector collisionDetector;
    PShader lightShader, lightTextureShader;

    PShape sandFloor;
    private void init() {
        textures = loadImage("src/resources/assets/textures.png");
        //lightTextureShader = loadShader("src/resources/shaders/lightTextureFrag.glsl", "src/resources/shaders/lightTextureVert.glsl");
        lightTextureShader = loadShader("src/resources/shaders/lightTextureFragRealistic.glsl", "src/resources/shaders/lightTextureVertRealistic.glsl");
        lightShader = loadShader("src/resources/shaders/lightFrag.glsl", "src/resources/shaders/lightVert.glsl");
    
        writer.setLevelHeight(2);
        writer.setPyramidSize(21);
        // dependency injection
        sF = new ShapeFactory(this, textures);
        PVector startingPos = new PVector(reader.getCellSize() + reader.getCellSize()/2, reader.getCellSize(), 0);
        
        pyramid = new Pyramid(reader.getPyramidSize(), reader.getCellSize(), reader.getLevelHeight(), this);
        pyEx = new PyramidExterior(this, reader.getPyramidSize(), reader.getCellSize(), reader.getLevelHeight());
        pyEx.initShape();

        collisionDetector = new CollisionDetector(pyramid);
        camera = new Camera(this, startingPos, reader.getMouseSensitivity(), reader.getMoveSpeed(), collisionDetector);
        frustum = new Frustum(camera); 
    
        sandFloor = ShapeFactory.sandFloor(1200f);
    }

    public void settings() {
        System.setProperty("jogl.disable.openglcore", "false");          // fix OpenGL stuff
                                                                         // we're on Processing 3
                                                                         // it's fixed in Processing 4
        if (reader.getFullScreen()) {
            fullScreen(P3D, 1);
        } else {
             size(reader.getScreenWidth(), reader.getScreenHeight(), P3D);

        }
    }

    public void setup() {
        writer.setFps(60);
        //noCursor();   // PLEASE DO NOT UNCOMMENT THIS. IDK 
        frameRate(reader.getFps());
        randomSeed(2);
        init();
    }
        
    public void draw() {
        background(240, 255, 255);
        //lightFalloff(1, 0, 0);
        //directionalLight(255, 255, 255, 0, -1, 0);
        //lights();
        //pointLight(255, 255, 255, -50, 1000, -50);
        
        shader(lightTextureShader);

        lightTextureShader.set("isSunlit", 0.0f);
        System.out.println("FPS: " + frameRate);
        camera.updateCamera();
        frustum.updateFrustum(camera);
        pyramid.render(frustum);


        lightTextureShader.set("isSunlit", 1.0f);
        pyEx.render(frustum);
        shape(sandFloor);
    }
    
    public void keyPressed() {
        KeyInput.updateOnKeyPressed(this);
    }
    public void keyReleased() {
        KeyInput.updateOnKeyReleased(this);
    }

} 
