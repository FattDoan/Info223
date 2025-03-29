package processing;

import processing.core.PApplet;
import processing.core.PVector;

class Plane {
    // A(x- x0) + B(y - y0) + C(z - z0) = 0 or Ax + By + Cz = D or N . P = D
    // [A, B, C] is vNormal (N)
    // and D = vNormal . point
    private PVector N;
    private float D;

    public Plane() {
        N = new PVector(0, 0, 0);
        D = 0; 
    }
    public Plane(PVector N, PVector point) {
        this.N = N;
        this.D = point.dot(N);
    }

    public void set(PVector N, PVector point) {
        this.N = N;
        this.D = point.dot(N);
    }

    public PVector getNormal() {
        return N;
    }
    public float getOffset() {
        return D;
    }
    public float getSignedDistance(PVector point) {
        return N.dot(point) - D;
    }

}
public class Frustum {
    private Plane[] planes = new Plane[6];
    //0 = near, 1 = far, 2 = left, 3 = right, 4 = top, 5 = bottom
   
    private final float halfVSide, halfHSide;
    private final PVector frontMultFar;

    public Frustum(Camera cam) {
        halfVSide = PApplet.tan(cam.fovY/2f) * cam.zNear;
        halfHSide = halfVSide * cam.aspect;
        frontMultFar = PVector.mult(cam.getForwardVect(), cam.zFar);
        for (int i = 0; i < 6; i++) {
            planes[i] = new Plane();
        }
    }
    public void updateFrustum(Camera cam) {
        planes[0].set(cam.getForwardVect(),
                      cam.getPos().add(PVector.mult(cam.getForwardVect(), cam.zNear)));
        planes[1].set(cam.getForwardVect().mult(-1),
                      cam.getPos().add(frontMultFar));
        
        PVector v0 = frontMultFar.copy().sub(PVector.mult(cam.getRightVect(), halfHSide));
        planes[2].set(cam.getUpVect().cross(v0),
                      cam.getPos());

        PVector v1 = frontMultFar.copy().add(PVector.mult(cam.getRightVect(), halfHSide));
        planes[3].set(v1.cross(cam.getUpVect()),
                      cam.getPos());
    
        PVector v2 = frontMultFar.copy().add(PVector.mult(cam.getUpVect(), halfVSide));
        planes[4].set(cam.getRightVect().cross(v2),
                      cam.getPos());

        PVector v3 = frontMultFar.copy().sub(PVector.mult(cam.getUpVect(), halfVSide));
        planes[5].set(v3.cross(cam.getRightVect()),
                      cam.getPos());
    }
    public Plane getPlane(int index) {
        return planes[index];
    }

}
