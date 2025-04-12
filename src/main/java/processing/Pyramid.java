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
    private Camera cam;                 // mainly to get cam pos

    public Pyramid(int pyramidSize, int cellSize, int levelHeight, Camera cam, PApplet context) {
        this.pyramidSize = pyramidSize;
        this.cellSize = cellSize;
        this.levelHeight = levelHeight;
        this.context = context;
        this.mazes = new ArrayList<Maze>(); 
        this.cam = cam;
        generatePyramid();
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
            changeLevel();
            level = getCurrentPlayerLevel();
            mazes.get(level).render();
        }
        else {
            mazes.get(0).render();
        }
    }

    public PVector getCamPos() {
        return cam.getPos().copy();
    }
    
    // OOP War-crime
    public Camera getCam() {
        return cam;
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
    public void changeLevel() {
        if (!CollisionDetector.isInPyramid(this, cam.getPos())) {
            return;  
        }
        PVector cellIndex = Maze.getCellIndex(cam.getPos(), cellSize, levelHeight);
        int i = (int)cellIndex.x, j = (int)cellIndex.y, level = (int)cellIndex.z;
        Maze m = mazes.get(level);
        // start (0,1)
        // end (mS - 2, mS - 1)
        if (i == m.getMazeSize()-2 && j == m.getMazeSize()-1 && level < mazes.size() - 2) {
            // Go to the start of the next maze
            PVector nextMazeStartIndex = (mazes.get(level+1)).getNeighBourCellIndex(0, 1, false);
            PVector nextMazeStart = Maze.getCellCoord((int)nextMazeStartIndex.x, (int)nextMazeStartIndex.y, level + 1, cellSize, levelHeight);
            nextMazeStart.add(cellSize/2, cellSize, cellSize/2);
            cam.resetCam(nextMazeStart, nextMazeStart.add(0, 0, cam.lookDistance));
            return;
        }
        if (level > 0 && i == 0 && j == 1) {
            // Go to the end of the previous maze
            Maze prevMaze = mazes.get(level - 1);
            PVector prevMazeEndIndex = prevMaze.getNeighBourCellIndex(prevMaze.getMazeSize() - 2, prevMaze.getMazeSize() - 1, false);            
            PVector prevMazeEnd = Maze.getCellCoord((int)prevMazeEndIndex.x, (int)prevMazeEndIndex.y, level - 1, cellSize, levelHeight);
            prevMazeEnd.add(cellSize/2, cellSize, cellSize/2);
            cam.resetCam(prevMazeEnd, prevMazeEnd.add(0, 0, -cam.lookDistance));
            return;
        }
        
    }
}
