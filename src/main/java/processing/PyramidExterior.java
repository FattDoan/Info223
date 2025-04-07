package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import java.util.ArrayList;

class ExteriorWall extends AABB {
    private PShape S;
    private int w, h;
    public ExteriorWall(PVector min, PVector max, int h, int w) {
        super(min, max);
        this.h = h;
        this.w = w;
    }
    public PShape initShape() {
        int l = (int) (getMax().x - getMin().x);
        this.S = ShapeFactory.py_boxRing(l, this.h, this.w);
        this.S.translate(getMin().x, getMin().y, getMin().z);
        System.out.println("Inited wall");
        return this.S;
    }
    @Override
    public void update(Frustum frustum) {
        /*
        if (isOnFrustum(frustum)) {
            S.setVisible(true);
            System.out.println("Wall is on frustum");
        }
        else {
            System.out.println("Wall is not on frustum");
            S.setVisible(false);
        }*/
    }
}
public class PyramidExterior {
    private PApplet context;
    private int pyramidSize;            
    private int cellSize;
    private int levelHeight;
    private PShape S;
    private ArrayList <ExteriorWall> walls = new ArrayList<ExteriorWall>();
    public PyramidExterior(PApplet context, int pyramidSize, int cellSize, int levelHeight) {
        this.context = context;
        this.pyramidSize = pyramidSize;
        this.cellSize = cellSize;
        this.levelHeight = levelHeight;
    }
    public PShape initShape() {
        this.S = context.createShape(PApplet.GROUP);
        int nbMazes = (pyramidSize - 1) / 2;
        int w = 10;
        int h = w * levelHeight;
        int l = cellSize * (pyramidSize + 2);
        int nbLevels = (cellSize*levelHeight) / 20; // NOTE: celLSize must be divisible by 20 to render properly
        // At each level we decrease both ends by w * w * h (x * y * z)
        int X = -cellSize, Y = -cellSize, Z = 0;
        for (int i = 0; i < nbMazes; i++) {
            for (int level = 0; level < nbLevels; level++) {
                PVector min = new PVector(X, Y, Z);
                PVector max = min.copy().add(l, l, h);
                System.out.println("Min : " + min.x + " " + min.y + " " + min.z);
                System.out.println("Max : " + max.x + " " + max.y + " " + max.z);
                System.out.println("------------------------------------------------");
                ExteriorWall wall = new ExteriorWall(min, max, h, w);
                walls.add(wall);
                System.out.println("Added wall");
                S.addChild(wall.initShape());
                System.out.println("Added child");
                X += w; Y += w; Z += h;
                l -= 2*w;
            }
        }
        S.rotateX(-PApplet.PI/2);
        S.translate(0,0, cellSize * pyramidSize);
        return this.S;
    }
    public void render(Frustum frustum) {
        for (ExteriorWall wall : walls) {
            wall.update(frustum);
        }
        context.shape(S);
    }
}
