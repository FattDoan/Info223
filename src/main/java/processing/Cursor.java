package processing;

import processing.core.PApplet;
import processing.core.PVector;
import com.jogamp.newt.opengl.GLWindow;

// TODO: Cursor movement is really janky at high FPS (120+)

public class Cursor {

    private PApplet context;
    private final GLWindow window; 
    private final int centerCursorX, centerCursorY;

    private PVector accumulatedMovement = new PVector(0, 0);
    private PVector previousMovement = new PVector(0, 0);
    private final float dampingFactor = 0.6f; // Higher values = less damping
    private boolean isFirstUpdate = true; 
     
    private float deltaTime;
    
    public Cursor(PApplet context) {
        this.context = context;

        window = (GLWindow)context.getSurface().getNative();
        window.confinePointer(true);
        window.setPointerVisible(false);
   
        centerCursorX = context.width/2;
        centerCursorY = context.height/2;
    }
    
    private PVector mouseDelta() {
        return new PVector(context.mouseX - centerCursorX, context.mouseY - centerCursorY).copy();
        // if -pmouseX -pmouseY doesn't work, still dont know why. It should be the same
    }
    private void setCursorCenterScreen() {
        window.warpPointer(centerCursorX, centerCursorY);
    }
    public void updateCursorMovement() {
        PVector currentMovement = mouseDelta();
        // Skip processing on first update to avoid initial jump
        // And reset pointer position without applying rotation
        if (isFirstUpdate) {
            isFirstUpdate = false;
            return;
        }
        
        deltaTime = 1.0f / context.frameRate; // Avoid division by zero if FPS drops too low
        float deadZone = PApplet.map(context.frameRate, 0f, 240f, 1.2f, 0.05f);
        
        // Apply a dead zone to filter out tiny movements
        if (Math.abs(currentMovement.x) < deadZone) currentMovement.x = 0;
        if (Math.abs(currentMovement.y) < deadZone) currentMovement.y = 0;  
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
        // Store for next frame
        previousMovement = currentMovement.copy();
        setCursorCenterScreen();
    }
    public PVector getCursorMovement() {
        updateCursorMovement();
        return accumulatedMovement.copy();
    }
}
