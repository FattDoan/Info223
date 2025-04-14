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
    public PShape initShape(boolean hasGap, float doorX, float doorWidth) {
        int l = (int) (getMax().x - getMin().x);
        if (hasGap) {
            this.S = ShapeFactory.py_boxRingWithDoor(l, this.h, this.w, doorX, doorWidth);
        } else {
            this.S = ShapeFactory.py_boxRing(l, this.h, this.w);
        }
        this.S.translate(getMin().x, getMin().y, getMin().z);
        return this.S;
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
        // At each level we decrease both ends by w * h * w (x * y * z)
        int X = -cellSize, Y = 0, Z = -cellSize;
        
        //Door
        float doorStartingX = 40, doorWidth = 40, doorX; 
        for (int i = 0; i <= nbMazes; i++) {
            for (int level = 0; level < nbLevels; level++) {
                PVector min = new PVector(X, Y, Z);
                PVector max = min.copy().add(l, h, l);
                ExteriorWall wall = new ExteriorWall(min, max, h, w);
                walls.add(wall);
                PShape s;
                doorX = Math.max(0,doorStartingX - X);
                if (i == 0) {
                    s = wall.initShape(true, doorX, doorWidth);
                } else {
                    s = wall.initShape(false, 0, 0);
                }
                S.addChild(s);
                X += w; Y += h; Z += w;
                l -= 2*w;
            }
        }
        // Add the door
        PShape s = ShapeFactory.doorSides(40, 2 * 40, h, w, doorStartingX, doorWidth);
        S.addChild(s);
        return this.S;
    }
    public void render() {
        context.shape(S);
    }
    public float[] getBoundLowestLevel() {
        float[] bounds = new float[4];
        bounds[0] = walls.get(0).getMin().x;
        bounds[1] = walls.get(0).getMax().x;
        bounds[2] = walls.get(0).getMin().z;
        bounds[3] = walls.get(0).getMax().z;
        return bounds;
    }
}
