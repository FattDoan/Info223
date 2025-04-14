package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PImage;

public class ShapeFactory {
    private static PApplet context;
    private static PImage textures;
    private static PImage brickTex;
    private static PImage sandTex;

    private static int stones_w, stones_h, stones_x, stones_y;
    private static int floor_w, floor_h, floor_x, floor_y;
    private static int brick_w, brick_h, brick_x, brick_y;
    private static int sand_w, sand_h, sand_x, sand_y;

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

        sand_w = 512;
        sand_h = 512;
        sand_x = 1024;
        sand_y = 0;

        brickTex = textures.get(brick_x, brick_y, brick_w, brick_h);
        sandTex = textures.get(sand_x, sand_y, sand_w, sand_h);
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
    public static PShape boxNormal(float w, float h, float d, int r, int g, int b) {
      PShape box = context.createShape();
      box.beginShape(PApplet.QUADS);
        box.fill(r, g, b);
      // Front face
      box.vertex(-w/2, -h/2, d/2);
      box.vertex(w/2, -h/2, d/2);
      box.vertex(w/2, h/2, d/2);
      box.vertex(-w/2, h/2, d/2);
      
      // Back face
      box.vertex(-w/2, -h/2, -d/2);
      box.vertex(w/2, -h/2, -d/2);
      box.vertex(w/2, h/2, -d/2);
      box.vertex(-w/2, h/2, -d/2);
      
      // Left face
      box.vertex(-w/2, -h/2, d/2);
      box.vertex(-w/2, -h/2, -d/2);
      box.vertex(-w/2, h/2, -d/2);
      box.vertex(-w/2, h/2, d/2);
      
      // Right face
      box.vertex(w/2, -h/2, d/2);
      box.vertex(w/2, -h/2, -d/2);
      box.vertex(w/2, h/2, -d/2);
      box.vertex(w/2, h/2, d/2);
      
      // Top face
      box.vertex(-w/2, -h/2, d/2);
      box.vertex(w/2, -h/2, d/2);
      box.vertex(w/2, -h/2, -d/2);
      box.vertex(-w/2, -h/2, -d/2);
      
      // Bottom face
      box.vertex(-w/2, h/2, d/2);
      box.vertex(w/2, h/2, d/2);
      box.vertex(w/2, h/2, -d/2);
      box.vertex(-w/2, h/2, -d/2);
      
      box.endShape();
      box.translate(w/2, h/2, d/2);

      
      return box;
    }
    public static PShape floorNormal(float w, float h, float d, int r, int g, int b) {
      PShape box = context.createShape();
      box.beginShape(PApplet.QUADS);
      box.fill(r, g, b);     
      // Back face
      box.vertex(-w/2, -h/2, -d/2);
      box.vertex(w/2, -h/2, -d/2);
      box.vertex(w/2, h/2, -d/2);
      box.vertex(-w/2, h/2, -d/2);

      box.endShape();
      box.translate(w/2, h/2, d/2);

      
      return box;
    }    
    // l is along x, w is along z, h is along y
    public static PShape py_box(float l, float h) {
        setRepeat();
        float ratio = (float) l / h;
        PShape s = context.createShape();  
        s.beginShape(PApplet.QUAD);
        s.texture(brickTex);
        s.tint(238,232,170);
        s.vertex(0, h, 0, 0, 0);
        s.vertex(l, h, 0, ratio, 0);
        s.vertex(l, 0, 0, ratio, 1);
        s.vertex(0, 0, 0, 0, 1);
        s.endShape();
        return s;
    }
    public static PShape py_1SideBoxRing(float l, float h , float w) {
        PShape S = context.createShape(PApplet.GROUP);
        PShape s0 = py_box(l, h);
        PShape s1 = py_box(l, w);
        s1.rotateX(PApplet.PI/2);
        s1.translate(0,h,0);
        S.addChild(s0); 
        S.addChild(s1);
        return S;
    }
    public static PShape py_1SideBoxRingDoor(float l, float h, float w, float doorX, float doorWidth) {
        PShape S = context.createShape(PApplet.GROUP);
        
        PShape s0 = context.createShape(PApplet.GROUP);
        PShape s0_left = py_box(doorX, h);
        PShape s0_right = py_box(l - doorX - doorWidth, h);
        s0_right.translate(doorX + doorWidth, 0, 0);
        
        s0.addChild(s0_left);
        s0.addChild(s0_right);

        PShape s1 = context.createShape(PApplet.GROUP);
        PShape s1_left = py_box(doorX, w);
        PShape s1_right = py_box(l - doorX - doorWidth, w);
        s1_right.translate(doorX + doorWidth, 0, 0);
        
        s1.addChild(s1_left);
        s1.addChild(s1_right);

        s1.rotateX(PApplet.PI/2);
        s1.translate(0, h, 0);

        S.addChild(s0); S.addChild(s1);
        return S;
    }
    public static PShape py_boxRing(int l, int h, int w) {
        PShape bR = context.createShape(PApplet.GROUP);
 
        // top
        PShape s0 = py_1SideBoxRing(l, h, w);
        
        // right
        PShape s1 = py_1SideBoxRing(l, h, w);
        s1.rotateY(-PApplet.PI/2);
        s1.translate(l, 0, 0);
        
        // left
        PShape s2 = py_1SideBoxRing(l, h, w);
        s2.rotateY(PApplet.PI/2);
        s2.translate(0, 0, l);
       
        // bottom
        PShape s3 = py_1SideBoxRing(l, h, w);
        s3.rotateY(-PApplet.PI);
        s3.translate(l, 0, l);
        
        bR.addChild(s0);
        bR.addChild(s1);
        bR.addChild(s2);
        bR.addChild(s3);
       
        return bR;
    }
    public static PShape py_boxRingWithDoor(int l, int h, int w, float doorX, float doorWidth) {
        PShape bR = context.createShape(PApplet.GROUP);
        // top
        PShape s0 = py_1SideBoxRingDoor(l, h, w, doorX, doorWidth);
        // right
        PShape s1 = py_1SideBoxRing(l, h, w);
        s1.rotateY(-PApplet.PI/2);
        s1.translate(l, 0, 0);
        // left
        PShape s2 = py_1SideBoxRing(l, h, w);
        s2.rotateY(PApplet.PI/2);
        s2.translate(0, 0, l);
        // bottom
        PShape s3 = py_1SideBoxRing(l, h, w);
        s3.rotateY(-PApplet.PI);
        s3.translate(l, 0, l);
        
        bR.addChild(s0);
        bR.addChild(s1);
        bR.addChild(s2);
        bR.addChild(s3);
       
        return bR;
    }
    // dz and w_step are along z axis
    // ly and h_step are along y axis
    public static PShape doorSide(int dz, int ly, int h_step, int w_step) {
        int nbSteps = ly / h_step;
        PShape S = context.createShape(PApplet.GROUP);
        for (int i = 0; i < nbSteps; i++) {
            int l = dz - i * w_step;
            int h = h_step;
            PShape s = ShapeFactory.py_box(l, h);
            s.rotateY(-PApplet.PI/2);
            s.translate(0, i * h_step, i * w_step);
            S.addChild(s);
        }
        return S;
    }
    public static PShape doorSides(int dz, int ly, int h_step, int w_step, float doorX, float doorWidth) {
        PShape S = context.createShape(PApplet.GROUP);
        PShape s0 = doorSide(dz, ly, h_step, w_step);
        s0.translate(doorX, 0, 0);
        PShape s1 = doorSide(dz, ly, h_step, w_step);
        s1.translate(doorX + doorWidth, 0, 0);

        S.addChild(s0);
        S.addChild(s1);

        S.translate(0, 0, -dz);
        return S;
    }
    public static PShape sandFloor(float floorSize, float[] pyExBounds) {
        setRepeat();
        context.noiseSeed(69);
        int cols = 100, rows = 100;
        float r = 0.5f;     // roughness
        float scl = 20;
        PShape S = context.createShape(PApplet.GROUP);
       
        pyExBounds[0] += 42;
        pyExBounds[2] += 42;
        for (int i = 0; i < cols - 1; i++) {
            for (int j = 0; j < rows - 1; j++) {
                float x0 = PApplet.map(i, 0, cols, -floorSize, floorSize);
                float z0 = PApplet.map(j, 0, rows, -floorSize, floorSize);
               
                float x1 = PApplet.map(i + 1, 0, cols, -floorSize, floorSize);
                float z1 = PApplet.map(j + 1, 0, rows, -floorSize, floorSize);
                
                
                if (pyExBounds[0] <= x0 && x1 <= pyExBounds[1] && pyExBounds[2] <= z0 && z1 <= pyExBounds[3]) {
                    continue;
                }

                float y0 = context.noise(i * r, j * r) * scl;
                float y1 = context.noise((i + 1) * r, j * r) * scl;
                float y2 = context.noise((i + 1) * r, (j + 1) * r) * scl;
                float y3 = context.noise(i * r, (j + 1) * r) * scl;
                
                // Random angle for texture coordinates
                float angle = context.random(PApplet.TWO_PI);
                float scale = context.random(0.8f, 1.2f);
                float[][] texCoords = getRotatedTexCoords(angle, scale);

                PShape s = context.createShape();
                s.beginShape(PApplet.QUADS);
                s.texture(sandTex);
                s.noStroke();

                s.vertex(x0, y0, z0, texCoords[0][0], texCoords[0][1]);
                s.vertex(x1, y1, z0, texCoords[1][0], texCoords[1][1]);
                s.vertex(x1, y2, z1, texCoords[2][0], texCoords[2][1]);
                s.vertex(x0, y3, z1, texCoords[3][0], texCoords[3][1]);
                s.endShape();

                S.addChild(s);
            }
        }
        S.translate(0,-10,0);
        return S;
    }
    private static float[][] getRotatedTexCoords(float angle, float scale) {
        float[][] coords = new float[4][2];
        
        // Center point for rotation
        float cx = 0.5f;
        float cy = 0.5f;
        
        // Original coordinates
        float[][] orig = {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
        // Apply rotation and scaling around center
        for (int i = 0; i < 4; i++) {
            float dx = (orig[i][0] - cx) * scale;
            float dy = (orig[i][1] - cy) * scale;
            
            coords[i][0] = cx + dx * PApplet.cos(angle) - dy * PApplet.sin(angle);
            coords[i][1] = cy + dx * PApplet.sin(angle) + dy * PApplet.cos(angle);
        }    
        return coords;
    }
    public static PShape circle(float radius, int detail, int r, int g, int b) {
        PShape circle = context.createShape();
        circle.beginShape();
        circle.noStroke();
        circle.fill(r, g, b);

        float angleStep = PApplet.TWO_PI / detail;

        for (int i = 0; i <= detail; i++) {
            float angle = i * angleStep;
            float x = PApplet.cos(angle) * radius;
            float y = PApplet.sin(angle) * radius;
            circle.vertex(x, y);
        }

        circle.endShape(PApplet.CLOSE);
        return circle;
    }
}

