package processing;

import processing.core.PApplet;
import processing.core.PShape;
import java.util.ArrayList;

public class Pyramid {
    private PApplet context;
    private ArrayList<Maze> mazes;
    private int pyramidSize;            // pyramid size is the size of the maze of the bottom level
    private int cellSize;
    private int levelHeight;
    private PShape P;

    public Pyramid(int pyramidSize, int cellSize, int levelHeight, PApplet context) {
        this.pyramidSize = pyramidSize;
        this.cellSize = cellSize;
        this.levelHeight = levelHeight;
        this.context = context;
        mazes = new ArrayList<Maze>();
        generatePyramid();
        initShape();
    }
    public void generatePyramid() {
        
        int nbMazes = (pyramidSize - 1) / 2;
        //int nbMazes = 0;
        boolean flag = false;
        for (int i = 0; i <= nbMazes; i++) {
            if (i > nbMazes/2) {
                flag = true;
            }
            mazes.add(new Maze(i, pyramidSize - i * 2, cellSize, levelHeight, context, flag));
        }
    }
    public void initShape() {
        P = context.createShape(PApplet.GROUP);
        for (Maze maze : mazes) {
            P.addChild(maze.initShape());
        }
    }
    public void render(Frustum frustum) {
       for (Maze maze : mazes) {
           maze.update(frustum);
        }
        context.shape(P); 
    }

}
