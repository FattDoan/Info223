package processing;

import processing.core.PApplet;
import processing.core.PShape;
import java.util.HashMap;

public class ShapeManager {
    private static ShapeManager instance = null;

    private PApplet context;
    private HashMap<String, PShape> shapes = new HashMap<String, PShape>();

    private int CELL_SIZE;

    public ShapeManager(PApplet context, int CELL_SIZE) {
        this.context = context; 
        this.CELL_SIZE = CELL_SIZE;
        initShapes();
    }

    public static ShapeManager getInstance(PApplet context, int CELL_SIZE) {
        if (instance == null) {
            instance = new ShapeManager(context, CELL_SIZE);
        }
        return instance;
    }
    public static ShapeManager reloadInstance(PApplet context, int CELL_SIZE) {
        instance = new ShapeManager(context, CELL_SIZE);
        return instance;
    }

    public static PShape getShape(String shapeName) {
        if (instance == null) {
            throw new RuntimeException("ShapeManager not initialized properly!!");
        }
        return instance.shapes.get(shapeName);
    }
    // Must always call square0 first
    private void createSquare0Shape() {
        PShape s = context.createShape();
        s.beginShape();
        s.fill(255, 255, 255);
        s.vertex(0, 0, 0);
        s.vertex(CELL_SIZE, 0,0);
        s.vertex(CELL_SIZE, CELL_SIZE,0);
        s.vertex(0, CELL_SIZE,0);
        s.endShape(PApplet.CLOSE);        
        shapes.put("square0", s);
    }
    private void createSquare1Shape() {
        PShape s = context.createShape();
        s.beginShape();
        s.fill(255, 255, 255);
        s.translate(0, 0, CELL_SIZE);
        s.vertex(0, 0, 0);
        s.vertex(CELL_SIZE, 0,0);
        s.vertex(CELL_SIZE, CELL_SIZE,0);
        s.vertex(0, CELL_SIZE,0);
        s.endShape(PApplet.CLOSE);   
        shapes.put("square1", s);
    }
    private void createSquare2Shape() {
        PShape s = context.createShape();
        s.beginShape();
        s.fill(255, 255, 255);
        s.rotateY(-PApplet.PI/2);
        s.vertex(0, 0, 0);
        s.vertex(CELL_SIZE, 0,0);
        s.vertex(CELL_SIZE, CELL_SIZE,0);
        s.vertex(0, CELL_SIZE,0);
        s.endShape(PApplet.CLOSE);
        shapes.put("square2", s);
    }
    private void createSquare3Shape() {
        PShape s = context.createShape();
        s.beginShape();
        s.fill(255, 255, 255);
        s.rotateY(-PApplet.PI/2);
        s.translate(CELL_SIZE, 0, 0);
        s.vertex(0, 0, 0);
        s.vertex(CELL_SIZE, 0,0);
        s.vertex(CELL_SIZE, CELL_SIZE,0);
        s.vertex(0, CELL_SIZE,0);
        s.endShape(PApplet.CLOSE);   

        shapes.put("square3", s);
    }
    private void createSquare4Shape() {
        PShape s = context.createShape();
        s.beginShape();
        s.fill(255, 255, 255);
        s.rotateX(PApplet.PI/2);
        s.vertex(0, 0, 0);
        s.vertex(CELL_SIZE, 0,0);
        s.vertex(CELL_SIZE, CELL_SIZE,0);
        s.vertex(0, CELL_SIZE,0);
        s.endShape(PApplet.CLOSE);   
        shapes.put("square4", s);
    }
    private void createSquare5Shape() {
        PShape s = context.createShape();
        s.beginShape(); 
        s.fill(255, 255, 255);
        s.rotateX(PApplet.PI/2);
        s.translate(0, CELL_SIZE, 0);
        s.vertex(0, 0, 0);
        s.vertex(CELL_SIZE, 0,0);
        s.vertex(CELL_SIZE, CELL_SIZE,0);
        s.vertex(0, CELL_SIZE,0);
        s.endShape(PApplet.CLOSE);   
        shapes.put("square5", s);
    }
    public void initShapes() {
        createSquare0Shape(); 
        createSquare1Shape();
        createSquare2Shape();
        createSquare3Shape();
        createSquare4Shape();
        createSquare5Shape();
    }
    
}

