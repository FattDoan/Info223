package processing;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PShape;

// square cell 
// in left-handed coord system
// (x, y, z) is top-left coord, the height and width is Cell.SIZE

class AABB {
    protected PVector center = new PVector(0,0,0);
    protected PVector extents = new PVector(0,0,0);
    public AABB(PVector min, PVector max) {
        this.center = (min.copy().add(max)).mult(0.5f);
        this.extents = max.copy().sub(this.center);;
    }
    public boolean isOnOrForwardPlane(Plane plane) {
        float r = extents.x * PApplet.abs(plane.getNormal().x) + extents.y * PApplet.abs(plane.getNormal().y) + extents.z * PApplet.abs(plane.getNormal().z);
        return -r <= plane.getSignedDistance(center);
    }
    public boolean isOnFrustum(Frustum frustum) {
        for (int i = 0; i < 6; i++) {
            if (!isOnOrForwardPlane(frustum.getPlane(i))) {
                return false;
            }
        }
        return true;
    }
}
abstract class Cell extends AABB {
    protected final PVector coord;
    protected final Maze maze;        // reference to the maze
                                     // with this dont need to store the size of cell in Cell class
    protected boolean sides[];
    protected PShape S;
    protected boolean shapesInitialized = false;

    public Cell(PVector coord, Maze maze) {
        super(coord.copy(), coord.copy().add(new PVector(maze.getCellSize(), maze.getLevelHeight() * maze.getCellSize(), maze.getCellSize())));
        this.coord = coord.copy();
        this.maze = maze;
        this.sides = new boolean[]{false, false, false, false};
    }
    public float getX() { return this.coord.x; }
    public float getY() { return this.coord.y; }
    public float getZ() { return this.coord.z; }
    public PVector getCoord() { return coord; }
    public float getSize() { return maze.getCellSize(); }
    public boolean getSides(int i) { return sides[i]; }
    public void setSides(int i, boolean val) { sides[i] = val; }
    // TODO: need bounding box for collision detection
    
    // update based on frustum
    public void update(Frustum frustum) {
        if (isOnFrustum(frustum)) S.setVisible(true);
        else S.setVisible(false);
    }
    
    // console output for debugging
    public abstract void print();

    // draw calls optimization
    public abstract PShape initShapes(PApplet context);

    public boolean isEmpty() {
        return this instanceof EmptyCell;
    }
    public boolean isPath() {
        return this instanceof PathCell;
    }
    public boolean isWall() {
        return this instanceof WallCell;
    }

}

// Concrete implementations for each cell type

// THIS ONLY USED FOR MAZE GENERATION 
class EmptyCell extends Cell {
    public EmptyCell(PVector coord, Maze maze) {
        super(coord, maze);
    }
   
    @Override
    public PShape initShapes(PApplet context) {
        PShape cellShape = context.createShape(PApplet.GROUP);

        float height = maze.getLevelHeight() * maze.getCellSize();

        cellShape.translate(getX(), getY(), getZ());
        cellShape.addChild(ShapeFactory.square0(getSize(), height));
        cellShape.addChild(ShapeFactory.square1(getSize(), height));
        cellShape.addChild(ShapeFactory.square2(getSize(), height));
        cellShape.addChild(ShapeFactory.square3(getSize(), height));
    
        PShape s = ShapeFactory.square5(getSize(), getSize());
        s.translate(0, getSize() * (maze.getLevelHeight() - 1), 0);
        cellShape.addChild(s);
        
        return this.S = cellShape;
    }

    @Override
    public void print() {
        System.out.print(".");
    }
}
class PathCell extends Cell {
    public PathCell(PVector coord, Maze maze) {
        super(coord, maze);
    }
   
    @Override
    public PShape initShapes(PApplet context) {
        PShape cellShape = context.createShape(PApplet.GROUP);
        cellShape.translate(getX(), getY(), getZ());

        // add floor
        PShape s4 = ShapeFactory.square4(getSize(), getSize());
        cellShape.addChild(s4);


        // add roof
        PShape s5 = ShapeFactory.square5(getSize(), getSize());
        s5.translate(0, getSize() * (maze.getLevelHeight() - 1), 0);
        cellShape.addChild(s5);
        
        return this.S = cellShape;
    } 

    @Override
    public void print() {
        System.out.print(" ");
    }
}

class WallCell extends Cell {
    public WallCell(PVector coord, Maze maze) {
        super(coord, maze);
    }
    
    @Override
    public PShape initShapes(PApplet context) {
        PVector idx = Maze.getCellIndex(coord, maze.getCellSize()).copy();
        int i = (int)idx.x, j = (int)idx.y;
        float height = maze.getLevelHeight() * maze.getCellSize();
        
        PShape cellShape = context.createShape(PApplet.GROUP);
                
        cellShape.translate(getX(), getY(), getZ());
        
        // top
        if (i == 0 || !maze.getCell(i-1,j).isWall()) {
            PShape s = ShapeFactory.square0(getSize(), height);
            if (sides[3]) {
                s.setFill(context.color(255,255,0)); //yellow
            }
            cellShape.addChild(s);
        }
        // right
        if (j+1 == maze.getMazeSize() || !maze.getCell(i,j+1).isWall()) {
            PShape s = ShapeFactory.square3(getSize(), height);
            if (sides[2]) {
                s.setFill(context.color(0,255,0)); //green
            }
            cellShape.addChild(s);
        }
        // bottom
        if (i+1 == maze.getMazeSize() || !maze.getCell(i+1,j).isWall()) {
            PShape s = ShapeFactory.square1(getSize(), height);
            if (sides[0]) {
                s.setFill(context.color(255,0,0)); //red
            }
            cellShape.addChild(s);
        }
        // left
        if (j == 0|| !maze.getCell(i,j-1).isWall()) {
            PShape s = ShapeFactory.square2(getSize(), height);
            if (sides[1]) {
                s.setFill(context.color(0,0,255)); //blue
            }
            cellShape.addChild(s);
        }
        
        // add the roof
        PShape s = ShapeFactory.square5(getSize(), getSize());
        s.translate(0, getSize() * (maze.getLevelHeight() - 1), 0);

        cellShape.addChild(s);
    
        return this.S = cellShape;
    }

    @Override
    public void print() {
        System.out.print("#");
    }
}

class StartCell extends PathCell {
    public StartCell(PVector coord, Maze maze) {
        super(coord, maze);
    }
    
    @Override
    public PShape initShapes(PApplet context) {
        super.initShapes(context);
        this.S.fill(0, 255, 0);
        return this.S;
    }
    @Override
    public void print() {
        System.out.print("S");
    }
}

class EndCell extends PathCell {
    public EndCell(PVector coord, Maze maze) {
        super(coord, maze);
    }
    
    @Override
    public PShape initShapes(PApplet context) {
        super.initShapes(context);
        this.S.fill(0, 255, 0);
        return this.S;
    }
    @Override
    public void print() {
        System.out.print("E");
    }
}

public class Maze {
    private PApplet context;
    private Cell[][] cells;
    private final int mazeSize;
    private final int cellSize;
    private final int level;
    private final int levelHeight;
    private PShape M;
    // level starting from 0 -> (PYRAMID_SIZE - 1)/2 -1 max
    public Maze(int level, int mazeSize, int cellSize, int levelHeight, PApplet context, boolean letEmpty) {
        this.context = context;
        this.level = level;
        this.mazeSize = mazeSize;
        this.cellSize = cellSize;
        this.levelHeight = levelHeight;

        cells = new Cell[mazeSize][mazeSize];
        if (!letEmpty) {
            mazeGenerationDefault();
            cellsSideCalculate();
        }
        else {
            for (int i = 0; i < mazeSize; i++) {
                for (int j = 0; j < mazeSize; j++) {
                    cells[i][j] = new EmptyCell(getCellCoord(i, j, level, cellSize), this);
                }
            }
        }
        initShape();
    }
    public int getCellSize() {
        return cellSize;
    }
    public int getMazeSize() {
        return mazeSize;
    }
    public int getLevelHeight() {
        return levelHeight;
    }
    public Cell getCell(int i, int j) {
        return cells[i][j];
    }
    public static PVector getCellCoord(int i, int j, int mazeLevel, int cellSize) {
        return new PVector((j + mazeLevel) * cellSize,
                           mazeLevel * cellSize * 2,
                           (i + mazeLevel) * cellSize);
    }

    public static PVector getCellIndex(PVector coord, int cellSize) {
        int mazeLevel = PApplet.floor(coord.y / (cellSize * 2)); 
        return new PVector(coord.z / cellSize - mazeLevel, 
                           coord.x / cellSize - mazeLevel,
                           mazeLevel);
    }

    public void mazeGenerationDefault() {
        int todig = 0;
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    cells[i][j] = new EmptyCell(getCellCoord(i, j, level, cellSize), this); // path
                    todig++;
                } else
                    cells[i][j] = new WallCell(getCellCoord(i, j, level, cellSize), this); // wall
            }
        }
        int gx = 1;
        int gy = 1;
        while (todig > 0) {
            int oldgx = gx;
            int oldgy = gy;
            int alea = PApplet.floor(context.random(0, 4)); // selon un tirage aleatoire
            if (alea == 0 && gx > 1) gx -= 2; // le fantome va a gauche
            else if (alea == 1 && gy > 1) gy -= 2; // le fantome va en haut
            else if (alea == 2 && gx < mazeSize - 2) gx += 2; // .. va a droite
            else if (alea == 3 && gy < mazeSize - 2) gy += 2; // .. va en bas

            if (cells[gy][gx].isEmpty()) {
                todig--;
                cells[gy][gx] = new PathCell(getCellCoord(gy, gx, level, cellSize), this);
                cells[(gy + oldgy) / 2][(gx + oldgx) / 2] = new PathCell(getCellCoord((gy + oldgy) / 2, (gx + oldgx) / 2, level, cellSize), this);
            }
        }

        cells[0][1] = new StartCell(getCellCoord(0, 1, level, cellSize), this);// entree
        cells[mazeSize - 2][mazeSize - 1] = new EndCell(getCellCoord(mazeSize - 2, mazeSize - 1, level, cellSize), this); // sortie
    }

    public void cellsSideCalculate() {
        for (int i = 1; i < mazeSize - 1; i++) {
            for (int j = 1; j < mazeSize - 1; j++) {
                if (cells[i][j].isPath()) {
                    if (cells[i - 1][j].isWall() && cells[i + 1][j].isPath() &&
                            cells[i][j - 1].isWall() && cells[i][j + 1].isWall())
                        cells[i-1][j].setSides(0, true);
                    if (cells[i - 1][j].isPath() && cells[i + 1][j].isWall() &&
                            cells[i][j - 1].isWall() && cells[i][j + 1].isWall())
                        cells[i+1][j].setSides(3, true);
                    if (cells[i - 1][j].isWall() && cells[i + 1][j].isWall() &&
                            cells[i][j - 1].isPath() && cells[i][j + 1].isWall())
                        cells[i][j+1].setSides(1, true);
                    if (cells[i - 1][j].isWall() && cells[i + 1][j].isWall() &&
                            cells[i][j - 1].isWall() && cells[i][j + 1].isPath())
                        cells[i][j-1].setSides(2, true);
                }
            }
        }
    }

    public PShape initShape() {
        M = context.createShape(PApplet.GROUP);
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                M.addChild(cells[i][j].initShapes(context));
            }
        }
        return M;
    }
    public void update(Frustum frustum) {
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                cells[i][j].update(frustum);
            }
        }
    }
    public void print() {
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                cells[i][j].print();
            }
            System.out.println();
        }
    }
}
