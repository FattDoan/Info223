package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PImage;

public class ShapeFactory {
    private static PApplet context;
    private static PImage stonesTex;

    public ShapeFactory(PApplet context, PImage stonesTex) {
        ShapeFactory.context = context;
        ShapeFactory.stonesTex = stonesTex;
    }

    // 6 faces of width * width * height block
    // width is  along x or z axis, height is along y axis
    public static PShape square0(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.normal(0f, 0f, -1f);
        s.texture(stonesTex);
        s.noStroke();
        //s.fill(255, 255, 255);
        s.vertex(0, 0, 0, 0, 0);
        s.vertex(width, 0,0, stonesTex.width, 0);
        s.vertex(width, height,0, stonesTex.width, stonesTex.height);
        s.vertex(0, height,0, 0, stonesTex.height);
        s.endShape();    
        return s;
    } 
    public static PShape square1(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.normal(0f, 0f, 1f); 
        s.texture(stonesTex);
        s.noStroke();
        //s.fill(255, 255, 255);
        s.vertex(0, 0, width, 0, 0);
        s.vertex(width, 0, width, stonesTex.width, 0);
        s.vertex(width, height, width, stonesTex.width, stonesTex.height);
        s.vertex(0, height, width, 0, stonesTex.height);
        s.endShape();    
        return s;
    } 
    public static PShape square2(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        //s.fill(255, 255, 255);
        s.normal(-1f, 0f, 0f);
        s.texture(stonesTex);
        s.noStroke();
        s.vertex(0, 0, width, 0, 0);
        s.vertex(0, 0, 0, stonesTex.width, 0); 
        s.vertex(0, height ,0, stonesTex.width, stonesTex.height);
        s.vertex(0, height, width, 0, stonesTex.height);
        s.endShape();    
        return s;
    }
    public static PShape square3(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        //s.fill(255, 255, 255);
        s.normal(1f, 0f, 0f);
        s.texture(stonesTex);
        s.noStroke();
        s.vertex(width, 0, width, 0, 0);
        s.vertex(width, 0, 0, stonesTex.width, 0);
        s.vertex(width, height, 0, stonesTex.width, stonesTex.height);
        s.vertex(width, height, width, 0, stonesTex.height); 
        s.endShape();    
        return s;
    } 
    public static PShape square4(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.normal(0f, 1f, 0f);
        s.fill(0);
        s.noStroke();
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
        s.normal(0f, -1f, 0f);
        s.fill(0);
        s.noStroke();
        s.vertex(0, height, 0);
        s.vertex(width, height, 0);
        s.vertex(width, height, width);
        s.vertex(0, height, width);
        s.endShape();    
        return s;
    } 
}

