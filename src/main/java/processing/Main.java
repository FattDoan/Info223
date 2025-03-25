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
    ShapeManager sM;
    Pyramid pyramid;

    private void init() {
        // dependency injection
        PVector startingPos = new PVector(reader.getCellSize() + reader.getCellSize()/2, reader.getCellSize(), 0);
        camera = new Camera(this, startingPos, reader.getMouseSensitivity(), reader.getMoveSpeed());
        sM = ShapeManager.getInstance(this, reader.getCellSize());
        pyramid = new Pyramid(reader.getPyramidSize(), reader.getCellSize(), this);
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
        //noCursor();   // PLEASE DO NOT UNCOMMENT THIS. IDK 
        lights();
        frameRate(reader.getFps());
        randomSeed(2);
        init();
    }
    
    public void draw() {
        background(220);
        System.out.println(frameRate);
        camera.updateCamera();
        pyramid.render();
    }
    
    public void keyPressed() {
        KeyInput.updateOnKeyPressed(this);
    }
    public void keyReleased() {
        KeyInput.updateOnKeyReleased(this);
    }

} 
