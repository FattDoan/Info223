package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PImage;

public class ShapeFactory {
    private static PApplet context;
    private static PImage textures;

    private static int stones_w, stones_h, stones_x, stones_y;
    private static int floor_w, floor_h, floor_x, floor_y;
    public ShapeFactory(PApplet context, PImage textures) {
        ShapeFactory.context = context;
        ShapeFactory.textures = textures;

        stones_w = 312;
        stones_h = 512;
        stones_x = 0;
        stones_y = 0;

        floor_w = 512;
        floor_h = 512;
        floor_x = 512;
        floor_y = 0;
    }

    // 6 faces of width * width * height block
    // width is  along x or z axis, height is along y axis
    public static PShape square0(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.normal(0f, 0f, -1f);
        s.texture(textures);
        s.noStroke();
        //s.fill(255, 255, 255);
        s.vertex(0, 0, 0, 0, 0);
        s.vertex(width, 0,0, stones_w, 0);
        s.vertex(width, height,0, stones_w, stones_h);
        s.vertex(0, height,0, 0, stones_h);
        s.endShape();    
        return s;
    } 
    public static PShape square1(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.normal(0f, 0f, 1f); 
        s.texture(textures);
        s.noStroke();
        //s.fill(255, 255, 255);
        s.vertex(0, 0, width, 0, 0);
        s.vertex(width, 0, width, stones_w, 0);
        s.vertex(width, height, width, stones_w, stones_h);
        s.vertex(0, height, width, 0, stones_h);
        s.endShape();    
        return s;
    } 
    public static PShape square2(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        //s.fill(255, 255, 255);
        s.normal(-1f, 0f, 0f);
        s.texture(textures);
        s.noStroke();
        s.vertex(0, 0, width, 0, 0);
        s.vertex(0, 0, 0, stones_w, 0); 
        s.vertex(0, height ,0, stones_w, stones_h);
        s.vertex(0, height, width, 0, stones_h);
        s.endShape();    
        return s;
    }
    public static PShape square3(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        //s.fill(255, 255, 255);
        s.normal(1f, 0f, 0f);
        s.texture(textures);
        s.noStroke();
        s.vertex(width, 0, width, 0, 0);
        s.vertex(width, 0, 0, stones_w, 0);
        s.vertex(width, height, 0, stones_w, stones_h);
        s.vertex(width, height, width, 0, stones_h); 
        s.endShape();    
        return s;
    } 
    public static PShape square4(float width, float height) {
        PShape s = context.createShape();
        s.beginShape(PApplet.QUADS);
        s.normal(0f, 1f, 0f);
        //s.fill(0);
        s.texture(textures);
        s.noStroke();
        s.vertex(0, 0, 0, floor_x, floor_y);
        s.vertex(width, 0, 0, floor_x + floor_w, floor_y);
        s.vertex(width, 0, width, floor_x + floor_w, floor_y + floor_h);
        s.vertex(0, 0, width, floor_x, floor_y + floor_h);
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

