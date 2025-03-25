package processing;

import processing.core.PApplet;
import java.util.ArrayList;

public class Pyramid {
    private PApplet context;
    private ArrayList<Maze> mazes;
    private int pyramidSize;            // pyramid size is the size of the maze of the bottom level
    private int cellSize; 
    public Pyramid(int pyramidSize, int cellSize, PApplet context) {
        this.pyramidSize = pyramidSize;
        this.cellSize = cellSize;
        this.context = context;
        mazes = new ArrayList<Maze>();
        generatePyramid();
    }
    public void generatePyramid() {
        int nbMazes = (pyramidSize - 1) / 2;
        boolean flag = false;
        for (int i = 0; i <= nbMazes; i++) {
            if (i > nbMazes/2) {
                flag = true;
            }
            mazes.add(new Maze(i, pyramidSize - i * 2, cellSize, context, flag));
        }
    }
    public void render() {
        for (Maze maze : mazes) {
            maze.render();
        }
    }

}
