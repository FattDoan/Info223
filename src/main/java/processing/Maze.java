package processing;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PShape;

public class Maze extends AABB {
    private PApplet context;
    private Cell[][] cells;
    private final int mazeSize;
    private final int cellSize;
    private final int level;
    private final int levelHeight;
    private PShape M;
    // level starting from 0 -> (PYRAMID_SIZE - 1)/2 -1 max
    public Maze(int level, int mazeSize, int cellSize, int levelHeight, PApplet context, boolean letEmpty) {
        super(new PVector(level * cellSize, level * levelHeight * cellSize, level * cellSize), 
              new PVector((level + mazeSize) * cellSize, (level + 1) * levelHeight * cellSize, (level + mazeSize) * cellSize));
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
                    cells[i][j] = new EmptyCell(getCellCoord(i, j, level, cellSize, levelHeight), this);
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
    public int getLevel() {
        return level;
    }
    public static PVector getCellCoord(int i, int j, int mazeLevel, int cellSize, int levelHeight) {
        return new PVector((j + mazeLevel) * cellSize,
                           mazeLevel * cellSize * levelHeight,
                           (i + mazeLevel) * cellSize);
    }

    // returns (i, j, mazeLevel) mazeLevel = [0..(PYRAMID_SIZE - 1)/2 - 1]
    public static PVector getCellIndex(PVector coord, int cellSize, int levelHeight) {
        int mazeLevel = PApplet.floor(coord.y / (cellSize * levelHeight)); 
        return new PVector(coord.z / cellSize - mazeLevel, 
                           coord.x / cellSize - mazeLevel,
                           mazeLevel);
    }

    public void mazeGenerationDefault() {
        int todig = 0;
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    cells[i][j] = new EmptyCell(getCellCoord(i, j, level, cellSize, levelHeight), this); // path
                    todig++;
                } else
                    cells[i][j] = new WallCell(getCellCoord(i, j, level, cellSize, levelHeight), this); // wall
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
                cells[gy][gx] = new PathCell(getCellCoord(gy, gx, level, cellSize, levelHeight), this);
                cells[(gy + oldgy) / 2][(gx + oldgx) / 2] = new PathCell(getCellCoord((gy + oldgy) / 2, (gx + oldgx) / 2, level, cellSize, levelHeight), this);
            }
        }

        cells[0][1] = new StartCell(getCellCoord(0, 1, level, cellSize, levelHeight), this);// entree
        cells[mazeSize - 2][mazeSize - 1] = new EndCell(getCellCoord(mazeSize - 2, mazeSize - 1, level, cellSize, levelHeight), this); // sortie
    
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

    public void render() {
        context.shape(M);
    }
    public void print() {
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                cells[i][j].print();
            }
            System.out.println();
        }
    }
    public PShape getShape() {
        return M;
    }
 
}
