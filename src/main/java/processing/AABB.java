package processing;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PMatrix3D;

public abstract class AABB {
    private PVector min = new PVector(0,0,0);
    private PVector max = new PVector(0,0,0);
    private PVector center = new PVector(0,0,0);
    private PVector extents = new PVector(0,0,0);
    public AABB(PVector min, PVector max) {
        this.min = min.copy();
        this.max = max.copy();
        updateCenterExtents(); 
    }
    private void updateCenterExtents() {
        this.center = (min.copy().add(max)).mult(0.5f);
        this.extents = max.copy().sub(this.center);
    }
    public boolean isOnOrForwardPlane(Plane plane) {
        float r = extents.x * PApplet.abs(plane.getNormal().x) + extents.y * PApplet.abs(plane.getNormal().y) + extents.z * PApplet.abs(plane.getNormal().z);
        return -r <= plane.getSignedDistance(center);
    }
    public boolean isOnFrustum(Frustum frustum) {
        for (int i = 0; i < 6; i++) {
            if (!isOnOrForwardPlane(frustum.getPlane(i))) {
                return false;
            }
        }
        return true;
    }
    public boolean isPointInsideAABB(PVector point) {
        return point.x >= min.x && point.x <= max.x &&
               point.y >= min.y && point.y <= max.y &&
               point.z >= min.z && point.z <= max.z;
    }
    public abstract void update(Frustum frustum);
    public PVector getMin() { return min; }
    public PVector getMax() { return max; }

    void updateWithMatrix(PMatrix3D transformMatrix) {       
        // Create all 8 corners of the box
        PVector[] corners = new PVector[8];
        corners[0] = new PVector(min.x, min.y, min.z);
        corners[1] = new PVector(max.x, min.y, min.z);
        corners[2] = new PVector(min.x, max.y, min.z);
        corners[3] = new PVector(max.x, max.y, min.z);
        corners[4] = new PVector(min.x, min.y, max.z);
        corners[5] = new PVector(max.x, min.y, max.z);
        corners[6] = new PVector(min.x, max.y, max.z);
        corners[7] = new PVector(max.x, max.y, max.z);
        
        // Apply transformation matrix to all corners
        for (int i = 0; i < corners.length; i++) {
            PVector transformed = new PVector();
            transformMatrix.mult(corners[i], transformed);
            corners[i] = transformed;
        }
        
        // Find new min and max
        PVector newMin = new PVector(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        PVector newMax = new PVector(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
        
        for (PVector corner : corners) {
            newMin.x = Math.min(newMin.x, corner.x);
            newMin.y = Math.min(newMin.y, corner.y);
            newMin.z = Math.min(newMin.z, corner.z);
            
            newMax.x = Math.max(newMax.x, corner.x);
            newMax.y = Math.max(newMax.y, corner.y);
            newMax.z = Math.max(newMax.z, corner.z);
        }
        
        min = newMin.copy();
        max = newMax.copy();
        updateCenterExtents();
    }

}
