package processing;

import processing.core.PVector;

public abstract class AABB {
    private PVector min = new PVector(0,0,0);
    private PVector max = new PVector(0,0,0);

    public AABB(PVector min, PVector max) {
        this.min = min.copy();
        this.max = max.copy();
    }

    /*
     * Student note: AABB was intended to be used primarily for frustum culling for even better performance
     * However, I have to scrap after realizing Processing does not support manipulating visibility of children of a PShape group
     *
     * for ex:
     * PShape parent = context.createShape(PApplet.GROUP);
     * PShape child1 = context.createShape(RECT, 0, 0, 10, 10);
     * PShape child2 = context.createShape(RECT, 150, 0, 100, 100);
     *
     * parent.addChild(child1);
     * parent.addChild(child2);
     *
     * parent.setVisible(true);
     * child1.setVisible(false);    
     *
     * shape(parent); ----> here child1 is still rendered despite its visibility being set to false
     *
     * I've dug deep through forums, javadoc and even looking through Procesing's source code but no luck
     * There's a hacky and convoluted workaround but that will just make the frustum culling performance gain negligible
     * (or even worse)
     * https://forum.processing.org/two/discussion/18965/setvisible.html
     *
     * I leave this piece of code here to pay homage for my 1 week of research of frustum culling
     *
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
    */
    public boolean isPointInsideAABB(PVector point) {
        return point.x >= min.x && point.x <= max.x &&
               point.y >= min.y && point.y <= max.y &&
               point.z >= min.z && point.z <= max.z;
    } 
    public PVector getMin() { return min; }
    public PVector getMax() { return max; }

}
