package processing;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet {
    public static void main(String[] args) {
        PApplet.main("processing.Main");
    }

    Configs.ConfigsReader reader = Configs.getReader();
    Configs.ConfigsWriter writer = Configs.getWriter(); 
    Camera camera;
    ShapeFactory sF = new ShapeFactory(this);
    Pyramid pyramid;
    Frustum frustum;
    private void init() {
        writer.setLevelHeight(2);
        writer.setPyramidSize(21);
        // dependency injection
        PVector startingPos = new PVector(reader.getCellSize() + reader.getCellSize()/2, reader.getCellSize(), 0);
        camera = new Camera(this, startingPos, reader.getMouseSensitivity(), reader.getMoveSpeed());
        frustum = new Frustum(camera);
        pyramid = new Pyramid(reader.getPyramidSize(), reader.getCellSize(), reader.getLevelHeight(), this);
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
        lights();
        frameRate(reader.getFps());
        randomSeed(2);
        init();
    }
    
    public void draw() {
        background(220);
        System.out.println("FPS: " + frameRate);
        camera.updateCamera();
        frustum.updateFrustum(camera);
        pyramid.render(frustum);

    }
    
    public void keyPressed() {
        KeyInput.updateOnKeyPressed(this);
    }
    public void keyReleased() {
        KeyInput.updateOnKeyReleased(this);
    }

} 
