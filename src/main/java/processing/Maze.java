package processing;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PShape;

// square cell 
// in left-handed coord system
// (x, y, z) is top-left coord, the height and width is Cell.SIZE
abstract class Cell {
    protected final PVector coord;
    protected final Maze maze;        // reference to the maze
                                    // with this dont need to store the size of cell in Cell class
    protected boolean sides[];
    public Cell(PVector coord, Maze maze) {
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
    
    // 3D rendering
    public abstract void render(PApplet context);

    // console output for debugging
    public abstract void print();

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
    public void render(PApplet context) {
        context.pushMatrix();
        context.translate(getX(), getY(), getZ());
        context.shape(ShapeManager.getShape("square0"));
        context.shape(ShapeManager.getShape("square1"));
        context.shape(ShapeManager.getShape("square2"));
        context.shape(ShapeManager.getShape("square3"));
        context.shape(ShapeManager.getShape("square4"));
        context.shape(ShapeManager.getShape("square5"));
        context.popMatrix(); 
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
    public void render(PApplet context) {
        context.pushMatrix();
        context.translate(getX(), getY(), getZ());
        context.shape(ShapeManager.getShape("square4"));
        context.popMatrix(); 
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
    public void render(PApplet context) {
        PVector idx = Maze.getCellIndex(coord, maze.getCellSize()).copy();
        int i = (int)idx.x, j = (int)idx.y, level = (int)idx.z;
        // top
        if (i == 0 || !maze.getCell(i-1,j).isWall())
        {
            PShape s = ShapeManager.getShape("square0");
            s.setFill(context.color(255,255,255)); //white
            if (sides[3]) {
                s.setFill(context.color(255,255,0)); //yellow
            }
            context.pushMatrix();
            context.translate(getX(), getY(), getZ()); 
            context.shape(s);
            context.popMatrix();
        }
        // right
        if (j+1 == maze.getMazeSize() || !maze.getCell(i,j+1).isWall())
        {
            PShape s = ShapeManager.getShape("square3");
            s.setFill(context.color(255,255,255)); //white
            if (sides[2]) {
                s.setFill(context.color(0,255,0)); //green
            }
            context.pushMatrix();
            context.translate(getX(), getY(), getZ());
            context.shape(s);
            context.popMatrix();
        }
        // bottom
        if (i+1 == maze.getMazeSize() || !maze.getCell(i+1,j).isWall())
        {
            PShape s = ShapeManager.getShape("square1");
            s.setFill(context.color(255,255,255)); //white
            if (sides[0]) {
                s.setFill(context.color(255,0,0)); //red
            }
            context.pushMatrix();
            context.translate(getX(), getY(), getZ());
            context.shape(s);
            context.popMatrix();
        }
        // left
        if (j == 0|| !maze.getCell(i,j-1).isWall())
        {
            PShape s = ShapeManager.getShape("square2");
            s.setFill(context.color(255,255,255)); //white
            if (sides[1]) {
                s.setFill(context.color(0,0,255)); //blue
            }
            context.pushMatrix();
            context.translate(getX(), getY(), getZ());
            context.shape(s);
            context.popMatrix();            
        }
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
    public void render(PApplet context) {
        context.fill(0, 255, 0);
        super.render(context);
        context.fill(255, 255, 255);
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
    public void render(PApplet context) {
        context.fill(0, 255, 0);
        super.render(context);
        context.fill(255, 255, 255);
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
    // level starting from 0 -> (PYRAMID_SIZE - 1)/2 -1 max
    public Maze(int level, int mazeSize, int cellSize, PApplet context, boolean letEmpty) {
        this.context = context;
        this.level = level;
        this.mazeSize = mazeSize;
        this.cellSize = cellSize;
        
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
    }
    public int getCellSize() {
        return cellSize;
    }
    public int getMazeSize() {
        return mazeSize;
    }
    public Cell getCell(int i, int j) {
        return cells[i][j];
    }
    public static PVector getCellCoord(int i, int j, int level, int cellSize) {
        return new PVector((j + level) * cellSize,
                           level * cellSize * 2,
                           (i + level) * cellSize);
    }
    // return (i, j, level)
    public static PVector getCellIndex(PVector coord, int cellSize) {
        int level = PApplet.floor(coord.y / (cellSize * 2));
        return new PVector(coord.z / cellSize - level, 
                           coord.x / cellSize - level,
                           level);
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
    public void render() {
        context.pushMatrix();
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                cells[i][j].render(context);
                if (cells[i][j].isWall() || cells[i][j].isEmpty()) {
                    context.pushMatrix();
                    context.translate(0, cellSize, 0);
                    cells[i][j].render(context);
                    context.popMatrix();
                }

                PVector coord = cells[i][j].getCoord().copy();
                context.pushMatrix();
                context.translate(coord.x, coord.y + cellSize * 2, coord.z);
                context.shape(ShapeManager.getShape("square4"));
                context.popMatrix(); 
                    
            }
        }
        context.popMatrix();
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
