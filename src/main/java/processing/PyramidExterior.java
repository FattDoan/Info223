package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

class ExteriorWall extends AABB {
    private PShape S;
    private int offsetHeight;
    public ExteriorWall(PVector min, PVector max, int offsetHeight) {
        super(min, max);
        this.offsetHeight = offsetHeight;
    }
    public void initShape() {
        int l = (int) (getMax().x - getMin().x);
        int w = (int) (getMax().y - getMin().y);
        int h = (int) (getMax().z - getMin().z);
        S = ShapeFactory.py_boxRing(l, w, h);
        S.translate(getMin().x, getMin().y + offsetHeight, getMin().z);
    }
    @Override
    public void update(Frustum frustum) {
        if (isOnFrustum(frustum)) S.setVisible(true);
        else S.setVisible(false);
    }
}
public class PyramidExterior {

}
