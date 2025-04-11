package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import java.util.ArrayList;

public class HUD {
    private PApplet context;
    private Pyramid pyramid;

    public HUD(PApplet context, Pyramid pyramid) {
        this.context = context;
        this.pyramid = pyramid;
        //   initShape();
    }
    /*
    private void initMap() {
        int n = pyramid.getNbMazes();
        for (int level = 0; level < n; level++) {
            System.out.println("Level " + level);
            Maze m = pyramid.getMaze(level);
            PShape s = context.createShape(PApplet.GROUP);
            System.out.println("Initializing maze " + level);
            for (int i = 0; i < m.getMazeSize(); i++) {
                for (int j = 0; j < m.getMazeSize(); j++) {
                    System.out.println("Checking...");
                    if (m.getCell(i, j).isWall()) {
                        PShape cell = ShapeFactory.boxNormal(10f,10f,10f,i*25,j*25,255-i*10+j*10);
                        PVector coord = Maze.getCellCoord(i, j, level, 10, m.getLevelHeight());
                        cell.translate(coord.x, coord.z, 0);
                        s.addChild(cell); 
                    }
                }
            }
            System.out.println("Finish maze " + level);
            s.translate(50, 50, 0);
            Map.add(s);
            System.out.println("Added maze");
        }
    }*/
    /*
    public void initShape() {
        initMap();
    }*/
    public void drawMap(Maze m) {
        PShape M = context.createShape(PApplet.GROUP);
        for (int i = 0; i < m.getMazeSize(); i++) {
            for (int j = 0; j < m.getMazeSize(); j++) {
                Cell c = m.getCell(i, j);
                if (c.isDiscovered()) {
                    PShape s;
                    if (c.isWall()) {
                        s = ShapeFactory.boxNormal(10f,10f,10f,i*25,j*25,255-i*10+j*10);
                    }
                    else {
                        s = ShapeFactory.floorNormal(10f, 10f, 10f, 128, 128, 128);
                    }
                    PVector coord = Maze.getCellCoord(i, j, m.getLevel(), 10, m.getLevelHeight());
                    s.translate(coord.x, coord.z, 0);
                    M.addChild(s);
                }
            }
        }
        PVector camPos = pyramid.getCamPos();

        PShape s = context.createShape(PApplet.SPHERE, 6);
        s.setFill(context.color(255, 0, 0));
        s.translate(camPos.x, camPos.z, 0);
        M.addChild(s);
        M.translate(25, 25, 0);
        M.rotateY(PApplet.PI/12);
        context.shape(M);
    }
    public void render() {
        //context.hint(PApplet.DISABLE_DEPTH_TEST);
        context.perspective();
        context.noLights();
        context.camera(context.width/2.f, context.height/2.f, 
                (context.height/2.f) / PApplet.tan(PApplet.PI*30.f / 180.f), 
                context.width/2.f, context.height/2.f, 0, 0, 1, 0);
        
        int i = pyramid.getCurrentPlayerLevel();
        if (i != -1) {
            context.stroke(0);
            System.out.println("Drawing hud");
            //context.shape(Map.get(i));
            drawMap(pyramid.getMaze(i));
        }
        //context.hint(PApplet.ENABLE_DEPTH_TEST);
    }
 
}
