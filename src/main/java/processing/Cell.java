package processing;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PShape;

// square cell 
// in left-handed coord system
// (x, y, z) is top-left coord, the height and width is Cell.SIZE


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
        PVector idx = Maze.getCellIndex(coord, maze.getCellSize(), maze.getLevelHeight()).copy();
        int i = (int)idx.x, j = (int)idx.y;
        float height = maze.getLevelHeight() * maze.getCellSize();
        
        PShape cellShape = context.createShape(PApplet.GROUP);
                
        cellShape.translate(getX(), getY(), getZ());
        
        // top
        if (i == 0 || !maze.getCell(i-1,j).isWall()) {
            PShape s = ShapeFactory.square0(getSize(), height);
            if (sides[3]) {
                s.setTint(context.color(255,255,0)); //yellow
            }
            cellShape.addChild(s);
        }
        // right
        if (j+1 == maze.getMazeSize() || !maze.getCell(i,j+1).isWall()) {
            PShape s = ShapeFactory.square3(getSize(), height);
            if (sides[2]) {
                s.setTint(context.color(0,255,0)); //green
            }
            cellShape.addChild(s);
        }
        // bottom
        if (i+1 == maze.getMazeSize() || !maze.getCell(i+1,j).isWall()) {
            PShape s = ShapeFactory.square1(getSize(), height);
            if (sides[0]) {
                s.setTint(context.color(255,0,0)); //red
            }
            cellShape.addChild(s);
        }
        // left
        if (j == 0|| !maze.getCell(i,j-1).isWall()) {
            PShape s = ShapeFactory.square2(getSize(), height);
            if (sides[1]) {
                s.setTint(context.color(0,0,255)); //blue
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
