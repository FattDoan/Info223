package processing;

import processing.core.PApplet;
import processing.core.PVector;

public class Camera {

    private PApplet context;
    private Cursor cursor;
    private CollisionDetector collisionDetector;

    private PVector pos;        // Position of the camera (main character)
    private PVector target;     // Where we're looking directly at
    private float theta;        // yaw: left and right rotation along X axis
    private float phi;          // pitch: up and down rotation along Y axis
    private float sensitivity;
    private float moveSpeed;
    
    private PVector forwardVect = new PVector(0, 0, 1); // for Z movement                                                            
    private PVector rightVect = new PVector(1, 0, 0); // for X movement
    private PVector upVect = new PVector(0, 1, 0); // for Y movement 
    
    public final float lookDistance = 4;
    public final float fovY = PApplet.PI/3f;
    public final float aspect;    
    public final float zNear;
    public final float zFar;

    private int lastFrame = 0;
    private float deltaTime = 0;

    public Camera(PApplet context, PVector startingPos, float sensitivity, float moveSpeed, CollisionDetector collisionDetector) {
        this.context = context;
        this.collisionDetector = collisionDetector;  // can be null if in dev mode

        this.sensitivity = sensitivity;
        this.moveSpeed = moveSpeed;
        this.pos = startingPos.copy();
        this.target = startingPos.copy(); this.target.z += lookDistance;
        this.theta = this.phi = 0;     // look straight ahead          
        this.cursor = new Cursor(context);

        this.zFar = 2000f;
        this.zNear = 0.1f;

        this.aspect = (float)context.width / context.height;
    }
    public PVector getPos() {
        return this.pos.copy();
    }
    public void setPos(PVector pos) {
        this.pos.set(pos.x, pos.y, pos.z);
    }
    public void setTarget(PVector target) {
        this.target.set(target.x, target.y, target.z);
    }
    public void resetCam(PVector pos, PVector target) {
        setPos(pos);
        setTarget(target);
        forwardVect = new PVector(0, 0, 1);                                                   
        rightVect = new PVector(1, 0, 0); 
        upVect = new PVector(0, 1, 0);  
        this.theta = this.phi = 0;     
    }
    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }
    public float getSensitivity() {
        return this.sensitivity;
    }
    public PVector getForwardVect() {
        return this.forwardVect.copy();
    }
    public PVector getRightVect() {
        return this.rightVect.copy();
    }
    public PVector getUpVect() {
        return this.upVect.copy();
    }

    public void updateCamera() { 
        PVector cursorMovement = cursor.getCursorMovement();
        // Apply to rotation with sensitivity
        this.theta += cursorMovement.x * sensitivity;
        this.phi += cursorMovement.y * sensitivity;
        this.phi = PApplet.constrain(this.phi, -89.f, 89.f);
        
        // Trigonometric rotation implementation
        // https://en.wikipedia.org/wiki/Spherical_coordinate_system
        // https://learnopengl.com/Getting-started/Camera
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

        // In processing, coord is left-handed
        forwardVect.set(lookDir.x, 0, lookDir.z);
        PVector.cross(forwardVect, new PVector(0, -1, 0), rightVect);
        PVector.cross(forwardVect, rightVect, upVect);

        // preventing when looking straight up or down, we can't move
        forwardVect.normalize();    
        rightVect.normalize();
        upVect.normalize();

        updateOnKeyPressed();

        this.target.set(this.pos.x + lookDir.x*lookDistance, 
                        this.pos.y + lookDir.y*lookDistance,
                        this.pos.z + lookDir.z*lookDistance);

        context.camera(this.pos.x, this.pos.y, this.pos.z, 
                       this.target.x, this.target.y, this.target.z, 
                       0, -1, 0);
        context.perspective(fovY, aspect, zNear, zFar);

        context.spotLight(255, 255, 255, this.pos.x, this.pos.y, this.pos.z, 
                          lookDir.x, lookDir.y, lookDir.z, 
                          PApplet.PI/4, 10);

    }
    public void updateOnKeyPressed() {
        PVector moveDir = new PVector(0, 0, 0);
        int currentFrame = context.frameCount;
        deltaTime = (currentFrame - lastFrame) / context.frameRate;
        lastFrame = currentFrame;
        float camSpeed = moveSpeed * deltaTime;
        // Forward
        if (KeyInput.goForward()) {
            moveDir.add(PVector.mult(forwardVect, 1));
        }
        // Backward
        if (KeyInput.goBackward()) {
            moveDir.add(PVector.mult(forwardVect, -1));
        }
        // Left
        if (KeyInput.goLeft()) { 
            moveDir.add(PVector.mult(rightVect, -1));
        }
        // Right
        if (KeyInput.goRight()) {
            moveDir.add(PVector.mult(rightVect, 1));
        }
        if (KeyInput.flyUp()) {
            moveDir.add(PVector.mult(upVect, 1));
        }
        if (KeyInput.flyDown()) {
            moveDir.add(PVector.mult(upVect, -1));
        }
        if (moveDir.mag() > 0) {
            moveDir.normalize();
            moveDir.mult(camSpeed);
            if (this.collisionDetector != null) {
                this.pos = collisionDetector.resolveCollision(this.pos, moveDir).copy();
            } else {
                this.pos.add(moveDir);
            }
        }
    }
}
