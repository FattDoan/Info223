package processing;

import processing.core.PApplet;
import processing.core.PShape;

public class ShapeFactory {
    private static PApplet context;

    public ShapeFactory(PApplet context) {
        ShapeFactory.context = context;
    }

    // 6 faces of width * width * height block
    // width is  along x or z axis, height is along y axis
    public static PShape square0(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.fill(255, 255, 255);
        s.vertex(0, 0, 0);
        s.vertex(width, 0,0);
        s.vertex(width, height,0);
        s.vertex(0, height,0);
        s.endShape();    
        return s;
    } 
    public static PShape square1(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.fill(255, 255, 255);
        s.vertex(0, 0, width);
        s.vertex(width, 0, width);
        s.vertex(width, height, width);
        s.vertex(0, height, width);
        s.endShape();    
        return s;
    } 
    public static PShape square2(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.fill(255, 255, 255);
        s.vertex(0, 0, 0);
        s.vertex(0, 0, width);
        s.vertex(0, height, width);
        s.vertex(0, height ,0);
        s.endShape();    
        return s;
    }
    public static PShape square3(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.fill(255, 255, 255);
        s.vertex(width, 0, 0);
        s.vertex(width, 0, width);
        s.vertex(width, height, width);
        s.vertex(width, height, 0);
        s.endShape();    
        return s;
    } 
    public static PShape square4(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.fill(255, 255, 255);
        s.vertex(0, 0, 0);
        s.vertex(width, 0, 0);
        s.vertex(width, 0, width);
        s.vertex(0, 0, width);
        s.endShape();    
        return s;
    } 
    public static PShape square5(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.fill(255, 255, 255);
        s.vertex(0, height, 0);
        s.vertex(width, height, 0);
        s.vertex(width, height, width);
        s.vertex(0, height, width);
        s.endShape();    
        return s;
    } 
}

