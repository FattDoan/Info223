package processing;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class AABB {
    private PVector min = new PVector(0,0,0);
    private PVector max = new PVector(0,0,0);
    private PVector center = new PVector(0,0,0);
    private PVector extents = new PVector(0,0,0);
    public AABB(PVector min, PVector max) {
        this.min = min.copy();
        this.max = max.copy();
        this.center = (min.copy().add(max)).mult(0.5f);
        this.extents = max.copy().sub(this.center);;
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
}
