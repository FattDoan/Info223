package processing;

import processing.core.PApplet;
import processing.core.PVector;

class Plane {
    private PVector vNormal;
    private float distance;

    public Plane(PVector vNormal, float distance) {
        this.vNormal = vNormal;
        this.distance = distance;
    }

    public void setNormal(PVector vNormal) {
        this.vNormal = vNormal;
    }
    public void setDistance(float distance) {
        this.distance = distance;
    }
    public PVector getNormal() {
        return vNormal;
    }
    public float getDistance() {
        return distance;
    }

}
public class Frustum {
    Plane[] planes = new Plane[6];
    //0 = near, 1 = far, 2 = left, 3 = right, 4 = top, 5 = bottom
    
    public Frustum(Camera cam) {
        
    }

}
