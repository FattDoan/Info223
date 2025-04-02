package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PImage;

public class ShapeFactory {
    private static PApplet context;
    private static PImage textures;
    private static PImage brickTex;
    
    private static int stones_w, stones_h, stones_x, stones_y;
    private static int floor_w, floor_h, floor_x, floor_y;
    private static int brick_w, brick_h, brick_x, brick_y;
    
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

        brick_w = 512;
        brick_h = 256;
        brick_x = 0;
        brick_y = 512;

        brickTex = textures.get(brick_x, brick_y, brick_w, brick_h);
    }
    public static void setRepeat() {
        context.textureMode(PApplet.NORMAL);
        context.textureWrap(PApplet.REPEAT);
    }
    public static void setDefault() {
        context.textureMode(PApplet.IMAGE);
        context.textureWrap(PApplet.CLAMP);
    }
    // 6 faces of width * width * height block
    // width is  along x or z axis, height is along y axis
    public static PShape square0(float width, float height) {
        setDefault();
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
        setDefault();
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
        setDefault();
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
        setDefault();
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
        setDefault();
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
        setDefault();
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
    public static PShape py_box(int l, int h, int w) {
        setRepeat();
        float ratio = (float) l / w;
        PShape s = context.createShape();  
        s.beginShape(PApplet.QUAD);
        s.texture(brickTex);
        s.tint(238,232,170);
        s.vertex(0, 0, h, 0, 0);
        s.vertex(l, 0, h, ratio, 0);
        s.vertex(l, 0, 0, ratio, 1);
        s.vertex(0, 0, 0, 0, 1);
        s.endShape();
        return s;
    }
    public static PShape py_1SideBoxRing(int l, int h , int w) {
        PShape S = context.createShape(PApplet.GROUP);
        PShape s0 = py_box(l, h, w);
        PShape s1 = py_box(l, h, w);
        s1.rotateX(-PApplet.PI/2);
        s1.translate(0,0,h);
        S.addChild(s0); 
        S.addChild(s1);
        return S;
    }
    public static PShape py_boxRing(int l, int h, int w) {
        PShape bR = context.createShape(PApplet.GROUP);
  
        PShape s0 = py_1SideBoxRing(l, h, w);
        PShape s1 = py_1SideBoxRing(l, h, w);
        s1.rotateZ(PApplet.PI/2);
        s1.rotateY(-PApplet.PI/2);
        s1.translate(w, 0, h);
        PShape s2 = py_1SideBoxRing(l, h, w);
        s2.rotateZ(PApplet.PI/2);
        s2.translate(l, 0, 0);
        
        PShape s3 = py_1SideBoxRing(l, h, w);
        s3.rotateX(-PApplet.PI/2);
        s3.translate(0, l - w, h);
        bR.addChild(s0);
        bR.addChild(s1);
        bR.addChild(s2);
        bR.addChild(s3);
        
        return bR;
    }
}

