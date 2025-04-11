package processing;

import processing.core.PApplet;
import processing.core.PVector;

public class CollisionDetector {
    private Pyramid pyramid;

    public CollisionDetector(Pyramid pyramid) {
        this.pyramid = pyramid;
    }
    public static boolean isInPyramid(Pyramid pyramid, PVector pos) {
        if (pos.y < 0 || pos.y > pyramid.getLevelHeight() * pyramid.getCellSize() * pyramid.getNbMazes()) {
            return false;
        }
        int mazeLevel = PApplet.floor(pos.y / (pyramid.getCellSize() * pyramid.getLevelHeight()));  
        return pyramid.getMaze(mazeLevel).isPointInsideAABB(pos); 
    }
    public boolean isColliding(PVector pos) {
        if (!CollisionDetector.isInPyramid(this.pyramid, pos)) {
            return false;
        }
        //pseudo AABB box with pos the center
        PVector cellIndex0 = Maze.getCellIndex(PVector.add(pos, new PVector(4,0,4)), pyramid.getCellSize(), pyramid.getLevelHeight());
        PVector cellIndex1 = Maze.getCellIndex(PVector.add(pos, new PVector(4,0,-4)), pyramid.getCellSize(), pyramid.getLevelHeight());
        PVector cellIndex2 = Maze.getCellIndex(PVector.add(pos, new PVector(-4,0,4)), pyramid.getCellSize(), pyramid.getLevelHeight());
        PVector cellIndex3 = Maze.getCellIndex(PVector.add(pos, new PVector(-4,0,-4)), pyramid.getCellSize(), pyramid.getLevelHeight());

        return pyramid.getMaze((int)cellIndex0.z).getCell((int)cellIndex0.x, (int)cellIndex0.y).isWall() ||
               pyramid.getMaze((int)cellIndex1.z).getCell((int)cellIndex1.x, (int)cellIndex1.y).isWall() ||
               pyramid.getMaze((int)cellIndex2.z).getCell((int)cellIndex2.x, (int)cellIndex2.y).isWall() ||
               pyramid.getMaze((int)cellIndex3.z).getCell((int)cellIndex3.x, (int)cellIndex3.y).isWall();
    }
    public PVector resolveCollision(PVector pos, PVector moveDir) {
        PVector newPos = pos.copy().add(moveDir);
        if (!isColliding(newPos)) {
            return newPos;
        }

        // Try moving along individual axes instead of diagonally
        PVector horizontalMove = new PVector(moveDir.x, 0, 0);
        PVector horizontalPos = pos.copy().add(horizontalMove);
        boolean horizontalCollision = isColliding(horizontalPos);
        
        PVector verticalMove = new PVector(0, moveDir.y, 0);
        PVector verticalPos = pos.copy().add(verticalMove);
        boolean verticalCollision = isColliding(verticalPos);
        
        PVector depthMove = new PVector(0, 0, moveDir.z);
        PVector depthPos = pos.copy().add(depthMove);
        boolean depthCollision = isColliding(depthPos);
        
        // Create a new movement vector with only the non-colliding components
        PVector safeMove = new PVector(
            horizontalCollision ? 0 : moveDir.x,
            verticalCollision ? 0 : moveDir.y,
            depthCollision ? 0 : moveDir.z
        );
        
        // Return the safe position
        return pos.copy().add(safeMove);
    }
}
