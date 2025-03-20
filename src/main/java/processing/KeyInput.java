package processing;

import processing.core.PApplet;

public final class KeyInput {
    // TODO : accomodate for AZERTY keyboards
 
    private static boolean goForward = false;
    private static boolean goBackward = false;
    private static boolean goLeft = false;
    private static boolean goRight = false;

    public static boolean goForward() { return goForward; }
    public static boolean goBackward() { return goBackward; }
    public static boolean goLeft() { return goLeft; }
    public static boolean goRight() { return goRight; }

    public static void updateOnKeyPressed(PApplet context) {
        if (context.keyCode == PApplet.UP || context.key == 'w' || context.key == 'W') {
            goForward = true;
            goBackward = false;
        }
        if (context.keyCode == PApplet.DOWN || context.key == 's' || context.key == 'S') {
            goBackward = true;
            goForward = false;
        }
        if (context.keyCode == PApplet.LEFT || context.key == 'a' || context.key == 'A') {
            goLeft = true;
            goRight = false;
        }
        if (context.keyCode == PApplet.RIGHT || context.key== 'd' || context.key == 'D') {
            goRight = true;
            goLeft = false;
        }
    }
    public static void updateOnKeyReleased(PApplet context) {
        if (context.keyCode == PApplet.UP || context.key == 'w' || context.key == 'W') goForward = false;
        if (context.keyCode == PApplet.DOWN || context.key == 's' || context.key == 'S') goBackward = false;
        if (context.keyCode == PApplet.LEFT || context.key == 'a' || context.key == 'A') goLeft = false;
        if (context.keyCode == PApplet.RIGHT || context.key== 'd' || context.key == 'D') goRight = false;
    }
}
