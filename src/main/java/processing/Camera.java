package processing;

import processing.core.PApplet;
import processing.core.PVector;
import org.apache.commons.math3.linear.*;
import com.jogamp.newt.opengl.GLWindow;

public class Camera {

    private PApplet context;

    private PVector pos;        // Position of the camera (main character)
    private PVector target;     // Where we're looking directly at
    private float theta;        // yaw: left and right rotation along X axis
    private float phi;          // pitch: up and down rotation along Y axis
    private float sensitivity = 0.05f;
    private int centerCursorX, centerCursorY;
    private final float lookDistance = 5;
    private final GLWindow window;

    public Camera(PApplet context, PVector startingPos) {
        this.context = context;
        this.pos = startingPos.copy();
        this.target = startingPos.copy(); this.target.z += lookDistance;
        this.theta = this.phi = 0;     // look straight ahead          
        this.centerCursorX = context.width/2;
        this.centerCursorY = context.height/2;
        window = (GLWindow)context.getSurface().getNative();
        window.confinePointer(true);
        window.setPointerVisible(false);
        //context.noCursor();
    }
    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }
    public float getSensitivity() {
        return this.sensitivity;
    }
    private PVector mouseDelta() {
        //return new PVector((context.mouseX - context.pmouseX), 
        //                   (context.mouseY - context.pmouseY));
        return new PVector((context.mouseX - centerCursorX), 
                           (context.mouseY - centerCursorY));
    }
    private float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }
    private void updateRotationsLinear() {
        PVector deltaMouseMovement = mouseDelta().copy();
        if (deltaMouseMovement.x != 0 || deltaMouseMovement.y != 0) {
            //deltaMouseMovement.x = lerp(0, deltaMouseMovement.x, 0.5f);
            //deltaMouseMovement.y = lerp(0, deltaMouseMovement.y, 0.5f);
            this.theta += deltaMouseMovement.x * sensitivity; 
            this.phi += deltaMouseMovement.y * sensitivity; 
            this.phi = PApplet.constrain(this.phi, -89.f, 89.f);
        }
    }
    private PVector accumulatedMovement = new PVector(0, 0);
private PVector previousMovement = new PVector(0, 0);
private float dampingFactor = 0.6f; // Higher values = less damping
private boolean isFirstUpdate = true;    
private void updateRotationsCurve() {
      PVector currentMovement = mouseDelta().copy();
    
    // Skip processing on first update to avoid initial jump
    // And reset pointer position without applying rotation
    if (isFirstUpdate) {
        isFirstUpdate = false;
        previousMovement = new PVector(0, 0);
        return;
    }
    
    // Apply a dead zone to filter out tiny movements
    if (Math.abs(currentMovement.x) < 1.0f) currentMovement.x = 0;
    if (Math.abs(currentMovement.y) < 1.0f) currentMovement.y = 0;
    
    // Blend previous and current movement with less weight on previous
    PVector blendedMovement = new PVector(
        currentMovement.x * 0.8f + previousMovement.x * 0.2f,
        currentMovement.y * 0.8f + previousMovement.y * 0.2f
    );
    
    // More gradual acceleration curve with lower sensitivity for small movements
    float magnitudeSq = blendedMovement.magSq();
    float scaleFactor = 1.0f;
    if (magnitudeSq > 0) {
        // Small movements get reduced more
        scaleFactor = PApplet.map((float)Math.sqrt(magnitudeSq), 0, 10, 0.3f, 0.9f);
        scaleFactor = PApplet.constrain(scaleFactor, 0.3f, 0.9f);
    }
    
    blendedMovement.mult(scaleFactor);
    
    // Apply damping to previous velocity
    accumulatedMovement.mult(dampingFactor);
    // Add new movement with reduced impact
    accumulatedMovement.add(blendedMovement.mult(0.7f));
    
    // Apply to rotation with reduced sensitivity
    this.theta += accumulatedMovement.x * (sensitivity * 0.5f);
    this.phi += accumulatedMovement.y * (sensitivity * 0.5f);
    this.phi = PApplet.constrain(this.phi, -89.f, 89.f);
    
    // Store for next frame
    previousMovement = currentMovement.copy();
}
    public void updateCamera() {
        System.out.println(context.frameRate);
        //float deltaTime = 1.0f / context.frameRate;
        //updateRotationsLinear();
        updateRotationsCurve();
 
        // Matrix rotation implementation
/*
        RealMatrix r = Rx(PApplet.radians(this.phi)).multiply(Ry(PApplet.radians(this.theta)));
        double[] forward = {0, 0, 1};
        forward = r.operate(forward);
        PVector lookDir = new PVector((float)forward[0], (float)forward[1], (float)forward[2]); 
*/        

        // Trigonometric rotation implementation
        // https://en.wikipedia.org/wiki/Spherical_coordinate_system
        // https://learnopengl.com/Getting-started/Camera
        // PVector lookDir = new PVector(
        // PApplet.cos(PApplet.radians(this.phi)) * PApplet.cos(PApplet.radians(this.theta)),
        //             PApplet.sin(PApplet.radians(this.phi)),
        //             PApplet.cos(PApplet.radians(this.phi)) * PApplet.sin(PApplet.radians(this.theta)));
        //
        // The above formula (from OpenGL) assumes if theta = phi = 0, we're looking at [1,0,0]
        // instead of [0,0,1] by our convention
        //
        // We can also try to Ry(theta) * Rx(phi) * [0,0,1] to get the same result
        PVector lookDir = new PVector(
            PApplet.cos(PApplet.radians(this.phi)) * PApplet.sin(PApplet.radians(this.theta)),
            -PApplet.sin(PApplet.radians(this.phi)),
            PApplet.cos(PApplet.radians(this.phi)) * PApplet.cos(PApplet.radians(this.theta))
                );

        lookDir = lookDir.normalize();
        System.out.println("lookDir: " + lookDir.x + " " + lookDir.y + " " + lookDir.z);
        this.target.set(this.pos.x + lookDir.x*lookDistance, 
                        this.pos.y + lookDir.y*lookDistance,
                        this.pos.z + lookDir.z*lookDistance);

        context.camera(this.pos.x, this.pos.y, this.pos.z, 
                       this.target.x, this.target.y, this.target.z, 
                       0, -1, 0);
        context.perspective(PApplet.PI/2f, (float)context.width/context.height, 1, 3000);
        window.warpPointer(centerCursorX, centerCursorY);
    }
    private RealMatrix Rx(float angle) {
        double[][] data = {{1, 0, 0},
                           {0, PApplet.cos(angle), -PApplet.sin(angle)},
                           {0, PApplet.sin(angle), PApplet.cos(angle)}};
        return new Array2DRowRealMatrix(data);
    }
    private RealMatrix Ry(float angle) {
        double[][] data = {{PApplet.cos(angle), 0, PApplet.sin(angle)},
                           {0, 1, 0},
                           {-PApplet.sin(angle), 0, PApplet.cos(angle)}};
        return new Array2DRowRealMatrix(data);
    }
}
