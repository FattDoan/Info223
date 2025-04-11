package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import java.util.ArrayList;

public class Pyramid {
    private PApplet context;
    private ArrayList<Maze> mazes;
    private int pyramidSize;            // pyramid size is the size of the maze of the bottom level
    private int cellSize;
    private int levelHeight;
    private PShape P;
    private Camera cam;                 // mainly to get cam pos

    public Pyramid(int pyramidSize, int cellSize, int levelHeight, Camera cam, PApplet context) {
        this.pyramidSize = pyramidSize;
        this.cellSize = cellSize;
        this.levelHeight = levelHeight;
        this.context = context;
        this.mazes = new ArrayList<Maze>(); 
        this.cam = cam;
        generatePyramid();
        initShape();
    }
    public void setCam(Camera cam) {
        this.cam = cam;
    }
    public Maze getMaze(int i) {
        return mazes.get(i);
    }
    public int getPyramidSize() {
        return pyramidSize;
    }
    public int getCellSize() {
        return cellSize;
    }
    public int getLevelHeight() {
        return levelHeight;
    }
    public int getNbMazes() {
        return (pyramidSize - 1) / 4 + 1;
    }
    public void generatePyramid() { 
        int nbMazes = (pyramidSize - 1) / 2;
        for (int i = 0; i <= nbMazes/2; i++) {
            mazes.add(new Maze(i, pyramidSize - i * 2, cellSize, levelHeight, context, false));
        }
    }
    public void initShape() {
        P = context.createShape(PApplet.GROUP);
        for (Maze maze : mazes) {
            P.addChild(maze.initShape());
        } 
    }

    public void render() {
        // update discorved cells
        // THIS IS BAD CODE. Update should not be put in here but deadline xd
        PVector camPos = getCamPos();
        int level = getCurrentPlayerLevel();
        if (level != -1) {
            Maze m = mazes.get(level);
            PVector cellIndex = Maze.getCellIndex(camPos, cellSize, levelHeight);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int x = (int)cellIndex.x + i;
                    int y = (int)cellIndex.y + j;
                    if (x >= 0 && x < m.getMazeSize() && y >= 0 && y < m.getMazeSize()) {
                        Cell c = m.getCell(x, y);
                        if (!c.isDiscovered()) {
                            c.setDiscovered(true);
                        }
                    }
                }
            } 
        }
        // render
        context.shape(P);  
    }

    public PVector getCamPos() {
        return cam.getPos().copy();
    }
    public int getCurrentPlayerLevel() {
        if (!CollisionDetector.isInPyramid(this, cam.getPos())) {
            return -1;  
        }
        else {
            PVector cellIndex = Maze.getCellIndex(cam.getPos(), cellSize, levelHeight);
            return (int)cellIndex.z;
        }
    }
    public Maze getCurrentPlayerMaze() {
        if (!CollisionDetector.isInPyramid(this, cam.getPos())) {
            return null;  
        }
        else {
            PVector cellIndex = Maze.getCellIndex(cam.getPos(), cellSize, levelHeight);
            int i = (int)cellIndex.z;
            return mazes.get(i);
        }
    }
}
