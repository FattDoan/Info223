package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class HUD {
    private PApplet context;
    private Pyramid pyramid;

    public HUD(PApplet context, Pyramid pyramid) {
        this.context = context;
        this.pyramid = pyramid;
        //   initShape();
    }

    private PShape createFOVCone(PVector position, PVector direction, float fovAngle, float coneLength) {
        PShape cone = context.createShape();
        
        // Calculate 2D direction (we only care about x and z for top-down map)
        PVector dir2D = new PVector(direction.x, direction.z);
        dir2D.normalize();
        
        // Calculate the angle of the direction vector
        float directionAngle = PApplet.atan2(dir2D.y, dir2D.x);
        
        float halfFOV = fovAngle / 2;
        float leftAngle = directionAngle - halfFOV;
        float rightAngle = directionAngle + halfFOV;
        
        PVector leftPoint = new PVector(
            position.x + coneLength * PApplet.cos(leftAngle),
            position.z + coneLength * PApplet.sin(leftAngle)
        );
        
        PVector rightPoint = new PVector(
            position.x + coneLength * PApplet.cos(rightAngle),
            position.z + coneLength * PApplet.sin(rightAngle)
        );
        

        cone.beginShape();
        cone.fill(255, 255, 0, 80); 
        cone.noStroke();
        cone.vertex(position.x, position.z);
        cone.vertex(leftPoint.x, leftPoint.y);
        cone.vertex(rightPoint.x, rightPoint.y);
        cone.endShape(PApplet.CLOSE);
        
        return cone;
    }
    // A lot of hard-coded values in here
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
        camPos.x = camPos.x * 10/m.getCellSize();
        camPos.z = camPos.z * 10/m.getCellSize();
        PShape player = context.createShape(PApplet.SPHERE, 4);
        player.setFill(context.color(0, 255, 0));
        player.translate(camPos.x, camPos.z, 0);
        player.setStroke(false);
        M.addChild(player);

        PShape fovCone = createFOVCone(camPos, pyramid.getCam().getForwardVect(), pyramid.getCam().fovY, 45);
        fovCone.translate(4, 4, 12);
        M.addChild(fovCone);
    

        M.translate(10, 40, 0);
        M.rotateY(PApplet.PI/12);
        M.rotateZ(-PApplet.PI/80);
        context.shape(M);
    }

    public void render() {
        context.pushMatrix();
         
        context.perspective(); 
        context.noLights();
        
        context.camera(context.width/2, context.height/2, 
                (context.height/2) / PApplet.tan(PApplet.PI*30.f / 180.f), 
                context.width/2, context.height/2, 0, 0, 1, 0);
        
        int i = pyramid.getCurrentPlayerLevel();
        String s = "Outside";
        if (i != -1) {
            context.stroke(0);
            s = "Level " + i;
            drawMap(pyramid.getMaze(i));
        }
        context.textSize(24);
        context.text(s, 10, 5, 100, 100);
        context.popMatrix();
    } 
}
