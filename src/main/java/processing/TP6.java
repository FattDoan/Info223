package processing;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class TP6 extends PApplet {
    public static void main(String[] args) {
        PApplet.main("processing.TP6");
    }


    int LAB_SIZE = 21;
    int BOX_SIZE = 40;
    char labyrinthe[][];
    char sides[][][];
    PShape floor;

    Camera camera;
    public void settings() {
        //size(1920, 1080, P3D);
        fullScreen(P3D, 1);
    }

    public void setup() {
        noCursor();
        frameRate(60);
        randomSeed(2);
        labyrinthe = new char[LAB_SIZE][LAB_SIZE];
        sides = new char[LAB_SIZE][LAB_SIZE][4];
        int todig = 0;
        for (int j = 0; j < LAB_SIZE; j++) {
            for (int i = 0; i < LAB_SIZE; i++) {
                sides[j][i][0] = 0;
                sides[j][i][1] = 0;
                sides[j][i][2] = 0;
                sides[j][i][3] = 0;
                if (j % 2 == 1 && i % 2 == 1) {
                    labyrinthe[j][i] = '.';
                    todig++;
                } else
                    labyrinthe[j][i] = '#';
            }
        }
        int gx = 1;
        int gy = 1;
        while (todig > 0) {
            int oldgx = gx;
            int oldgy = gy;
            int alea = floor(random(0, 4)); // selon un tirage aleatoire
            if (alea == 0 && gx > 1) gx -= 2; // le fantome va a gauche
            else if (alea == 1 && gy > 1) gy -= 2; // le fantome va en haut
            else if (alea == 2 && gx < LAB_SIZE - 2) gx += 2; // .. va a droite
            else if (alea == 3 && gy < LAB_SIZE - 2) gy += 2; // .. va en bas

            if (labyrinthe[gy][gx] == '.') {
                todig--;
                labyrinthe[gy][gx] = ' ';
                labyrinthe[(gy + oldgy) / 2][(gx + oldgx) / 2] = ' ';
            }
        }

        labyrinthe[0][1] = ' '; // entree
        labyrinthe[LAB_SIZE - 2][LAB_SIZE - 1] = ' '; // sortie

        for (int j = 1; j < LAB_SIZE - 1; j++) {
            for (int i = 1; i < LAB_SIZE - 1; i++) {
                if (labyrinthe[j][i] == ' ') {
                    if (labyrinthe[j - 1][i] == '#' && labyrinthe[j + 1][i] == ' ' &&
                            labyrinthe[j][i - 1] == '#' && labyrinthe[j][i + 1] == '#')
                        sides[j - 1][i][0] = 1;// c'est un bout de couloir vers le haut
                    if (labyrinthe[j - 1][i] == ' ' && labyrinthe[j + 1][i] == '#' &&
                            labyrinthe[j][i - 1] == '#' && labyrinthe[j][i + 1] == '#')
                        sides[j + 1][i][3] = 1;// c'est un bout de couloir vers le bas
                    if (labyrinthe[j - 1][i] == '#' && labyrinthe[j + 1][i] == '#' &&
                            labyrinthe[j][i - 1] == ' ' && labyrinthe[j][i + 1] == '#')
                        sides[j][i + 1][1] = 1;// c'est un bout de couloir vers la droite
                    if (labyrinthe[j - 1][i] == '#' && labyrinthe[j + 1][i] == '#' &&
                            labyrinthe[j][i - 1] == '#' && labyrinthe[j][i + 1] == ' ')
                        sides[j][i - 1][2] = 1;// c'est un bout de couloir vers la gauche
                }
            }
        }

        // un affichage texte pour vous aider a visualiser le labyrinthe en 2D
        for (int j = 0; j < LAB_SIZE; j++) {
            for (int i = 0; i < LAB_SIZE; i++) {
                print(labyrinthe[j][i]);
            }
            println("");
        }

        floor = createShape();
        floor.beginShape();
        floor.vertex(0, 0,0);
        floor.vertex(BOX_SIZE, 0,0);
        floor.vertex(BOX_SIZE, BOX_SIZE,0);
        floor.vertex(0, BOX_SIZE,0);
        floor.endShape(CLOSE);
    
        camera = new Camera(this, new PVector(BOX_SIZE + BOX_SIZE/2, BOX_SIZE, 0));
    }
    void drawAxes(float len) {
      strokeWeight(3);
      
      // X-Axis (Red)
      stroke(255, 0, 0);
      line(0, 0, 0, len, 0, 0);
      
      // Y-Axis (Green)
      stroke(0, 255, 0);
      line(0, 0, 0, 0, len, 0);
      
      // Z-Axis (Blue)
      stroke(0, 0, 255);
      line(0, 0, 0, 0, 0, len);
    
      stroke(0,0,0);
    }
 
    public void drawBox(int i, int j, int level) {
        beginShape(QUADS);
        // top
        if (0 == i || labyrinthe[i-1][j] != '#')
        {
            fill(255,255,255); //white
            if (sides[i][j][3] == 1) {
                fill(255, 255, 0); //yellow
            }
            vertex(0,0,BOX_SIZE * (level - 1));
            vertex(0,0,BOX_SIZE * level);
            vertex(BOX_SIZE,0,BOX_SIZE * level);
            vertex(BOX_SIZE,0,BOX_SIZE * (level - 1));
        }
        // right
        if (LAB_SIZE == j+1 || labyrinthe[i][j+1] != '#')
        {
            fill(255,255,255); //white
            if (sides[i][j][2] == 1) {
                fill(0,255,0); //green
            }
            vertex(BOX_SIZE, 0, BOX_SIZE * (level - 1));
            vertex(BOX_SIZE, 0, BOX_SIZE * level);
            vertex(BOX_SIZE, BOX_SIZE, BOX_SIZE * level);
            vertex(BOX_SIZE, BOX_SIZE, BOX_SIZE * (level - 1));
        }
        // bottom
        if (LAB_SIZE == i+1 || labyrinthe[i+1][j] != '#')
        {
            fill(255,255,255); //white
            if (sides[i][j][0] == 1) {
                fill(255,0,0); //red
            }
            vertex(BOX_SIZE, BOX_SIZE,BOX_SIZE * (level - 1));
            vertex(BOX_SIZE, BOX_SIZE, BOX_SIZE * level);
            vertex(0, BOX_SIZE, BOX_SIZE * level);
            vertex(0, BOX_SIZE,  BOX_SIZE * (level - 1));
        }
        // left
        if (0 == j || labyrinthe[i][j-1] != '#')
        {
            fill(255,255,255); //white
            if (sides[i][j][1] == 1) {
                fill(0,0,255); //blue
            }
            vertex(0, BOX_SIZE, BOX_SIZE * (level - 1));
            vertex(0, BOX_SIZE, BOX_SIZE * level);
            vertex(0, 0, BOX_SIZE * level);
            vertex(0, 0,  BOX_SIZE * (level - 1));
        }
        endShape();
    }

    /*
    float playerHeight = BOX_SIZE;
    // In 3D, z is y in 2D
    // 2D (x,y) -> 3D (x, height, y)
    PVector camPos = new PVector(BOX_SIZE + BOX_SIZE/2, playerHeight, 0);
    //PVector cursor =  new PVector(camPos.x, playerHeight, camPos.z + 1);
    PVector lookDir = new PVector(0, 0, 1); // Forward
    float yaw = 0, pitch = 0;
    float sensitivity = 0.0005f;
    float speed = 3.0f;
    */
    public boolean checkIfWallCollision(float x, float y) {
        int xI = (int) Math.floor(x), yI = (int) Math.floor(y);
        int i = xI % BOX_SIZE, j = yI % BOX_SIZE;
        return labyrinthe[i][j] == '#';
    }
    float a = 0;
    public void draw() {
        background(220);

        /*PVector deltaMouseMovement = new PVector(mouseX - pmouseX, mouseY - pmouseY);
        yaw += deltaMouseMovement.x * sensitivity;
        pitch += deltaMouseMovement.y * sensitivity;
        pitch = constrain(pitch, -PI/2 + 0.1f, PI/2 - 0.1f);

        lookDir.x = cos(yaw) * cos(pitch);
        lookDir.y = sin(pitch);
        lookDir.z = sin(yaw) * cos(pitch);

        PVector camTarget = PVector.add(camPos, lookDir);
        camera(camPos.x, camPos.y, camPos.z, camTarget.x, camTarget.y, camTarget.z, 0, -1, 0);
        perspective(PI/2.5f, (float)this.width/this.height, 1, 3000);*/
        //translate(width/2, height/2, 0);
        //rotateX(frameCount * 0.01f);
        //rotateY(frameCount * 0.01f);
        //rotateZ(-PI/2);
        //rotateY(a);

        //scale(0.4f, 0.4f, 0.4f);
        camera.updateCamera();
        pushMatrix();
        scale(1, 1, -1);
        for (int i = 0; i < LAB_SIZE; i++) {
            for (int j = 0; j < LAB_SIZE; j++) {
                pushMatrix();
                rotateX(-PI/2);
                translate(j*BOX_SIZE, i*BOX_SIZE);
                //rotateZ(PI/2);
                if (labyrinthe[i][j] == '#') {
                    drawBox(i,j,1);
                    drawBox(i,j,2);
                }
                else {
                    shape(floor);
                }
                popMatrix();
            }
        }
        popMatrix();

        //mouseX = width/2;
        //mouseY = height/2;
                //drawAxes(300);
        //cursor.x += deltaMouseMovement.x * sensitivity; cursor.y += deltaMouseMovement.y * sensitivity;
        //cursor.x += 0.5f;
    }
    public void keyPressed() {
        /*
        PVector moveDir = new PVector(0, 0, 0);
        if (key == 'w') moveDir.add(lookDir);
        if (key == 's') moveDir.sub(lookDir);
        if (key == 'a') moveDir.add(lookDir.cross(new PVector(0, 1, 0)));  // Left strafe
        if (key == 'd') moveDir.sub(lookDir.cross(new PVector(0, 1, 0)));  // Right strafe

        // Normalize direction and apply movement speed
        if (moveDir.mag() > 0) {
            moveDir.normalize().mult(speed);
            camPos.add(moveDir);
        }*/
    }
}
