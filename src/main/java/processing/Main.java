package processing;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PImage;
import processing.opengl.PShader;
import processing.core.PShape;

public class Main extends PApplet {
    public static void main(String[] args) {
        PApplet.main("processing.Main");
    }

    Configs.ConfigsReader reader = Configs.getReader();
    Configs.ConfigsWriter writer = Configs.getWriter(); 
    Camera camera;
    PImage textures;
    ShapeFactory sF;
    Pyramid pyramid;
    PyramidExterior pyEx;
    CollisionDetector collisionDetector;
    PShader lightTextureShader;

    PShape sandFloor;
    HUD hud;

    private void init() {
        textures = loadImage("assets/textures.png");
        lightTextureShader = loadShader("shaders/lightTextureFragRealistic.glsl", "shaders/lightTextureVertRealistic.glsl");
        lightTextureShader.set("hasTexture", 1.0f); 
        writer.setLevelHeight(2);
        writer.setPyramidSize(21);
        // dependency injection
        sF = new ShapeFactory(this, textures);
        PVector startingPos = new PVector(-100, reader.getCellSize(), -100);
        
        pyramid = new Pyramid(reader.getPyramidSize(), reader.getCellSize(), reader.getLevelHeight(), null, this);
        collisionDetector = new CollisionDetector(pyramid);
        camera = new Camera(this, startingPos, reader.getMouseSensitivity(), reader.getMoveSpeed(), collisionDetector);
        pyramid.setCam(camera);       // --> avoid dependency circularity

        pyEx = new PyramidExterior(this, reader.getPyramidSize(), reader.getCellSize(), reader.getLevelHeight());
        pyEx.initShape();
        sandFloor = ShapeFactory.sandFloor(1200f, pyEx.getBoundLowestLevel());
    
        hud = new HUD(this, pyramid);

        initMomie(); 
    }

    public void settings() {
        System.setProperty("jogl.disable.openglcore", "false");          // fix OpenGL stuff
                                                                         // we're on Processing 3
                                                                         // it's fixed in Processing 4
        if (reader.getFullScreen()) {
            fullScreen(P3D, 1);
        } else {
             size(reader.getScreenWidth(), reader.getScreenHeight(), P3D);

        }
    }

    public void setup() {
        writer.setFps(60);
        frameRate(reader.getFps());
        randomSeed(2);
        init();
    }
        
    public void draw() { 
        System.out.println("FPS: " + frameRate);
        background(203, 195, 227);
        
        pushMatrix();
        camera.updateCamera();
        shader(lightTextureShader);
        
        lightTextureShader.set("isSunlit", 0.0f);
        pyramid.render();

        lightTextureShader.set("hasTexture", 0.0f);
        renderMomie();
        
        lightTextureShader.set("hasTexture", 1.0f);
        lightTextureShader.set("isSunlit", 1.0f);
        pyEx.render();
        shape(sandFloor); 
        
       
        popMatrix();
        // Prepare for 2D rendering
        resetShader();
        hud.render();
    }
    
    public void keyPressed() {
        KeyInput.updateOnKeyPressed(this);
    }
    public void keyReleased() {
        KeyInput.updateOnKeyReleased(this);
    }

    // Momie integration, last resort
    // there's no clean code. If it works, it works
    float offsetFactor = 0.3f;

    float totalScale = 0.085f;
    float scaleFactor;
    float headScaleFactor;
    float baseScaleFactor = 0.6f;
    float baseHeadScaleFactor = 0.4f;
    
    // Variable qui contrôle le rayon global du corps
    // 1 = taille normale, <1 = plus fin, >1 = plus large
    float bodyThinness = 0.8f;
    // Variables d'offset pour les mains (modifiable facilement)
    float handOffsetLeftX = 150;  
    float handOffsetLeftY = 0;
    float handOffsetLeftZ = 0;
    float handOffsetRightX = -150;
    float handOffsetRightY = 0;
    float handOffsetRightZ = 0;

    // Déclaration des objets PShape pour les mains
    PShape hand1;
    PShape hand2;
    PShape mummy;
    boolean handsLoaded = false; // Flag pour vérifier si les mains sont correctement chargées

    public void renderMomie() {
      pushMatrix();
        int curLevel = pyramid.getCurrentPlayerLevel();
      if (curLevel > -1) {
          int mazeSize = pyramid.getMaze(curLevel).getMazeSize();
          PVector coord = Maze.getCellCoord(mazeSize-2, mazeSize-1, curLevel, pyramid.getCellSize(), pyramid.getLevelHeight()); 
          translate(coord.x, coord.y, coord.z);
      }
      translate(20, 30, 20);
      rotateX(-PI/2);
      rotateZ(PI/2);
        scale(totalScale);
    
    corps();
    tete();
    bras(-100, 100, 100, 0.5f);
    bras(100, 100,  100 , 0.5f);
    
    if (handsLoaded) {
      ajouterMain(-100, 100, 100, 0.5f, true, 
                  handOffsetLeftX, handOffsetLeftY, handOffsetLeftZ);
      ajouterMain(100, 100, 100, 0.5f, false, 
                  handOffsetRightX, handOffsetRightY, handOffsetRightZ);
    }
  popMatrix();

    }
    public void initMomie() {
      updateScaleFactors();
      
      // Chargement des fichiers OBJ des mains avec vérification d'erreurs
      try {
        hand1 = loadShape("assets/hand1.obj");
        hand2 = loadShape("assets/hand2.obj");
        
        if (hand1 != null && hand2 != null) {
          hand1.setFill(color(112, 108, 17));
          hand2.setFill(color(112, 108, 17));
          handsLoaded = true;
          println("Mains chargées avec succès");
        } else {
          println("Erreur: Impossible de charger les modèles de mains");
        }
      } catch (Exception e) {
        println("Exception lors du chargement des mains: " + e.getMessage());
      }
    }
    public void updateScaleFactors() {
        scaleFactor = baseScaleFactor;
        headScaleFactor = baseHeadScaleFactor; 
    }
    public void corps() {
      float kCorps = 13;
      float da = 4 * PI / 100;
      int nb_de_tours = 10;
      int iter_i = nb_de_tours * 50 / 2;
      
      for (int i = -iter_i; i <= iter_i; i++) {
        float a = i / 50.f * 2 * PI;
        float t = map(i, -iter_i, iter_i, -1, 1);
        float R1 = (150 + 100 * (1 - t * t)) * bodyThinness;
        float offset = R1 * offsetFactor;
        
        float x1 = R1 * cos(a) * scaleFactor;
        float y1 = R1 * sin(a) * scaleFactor;
        float z1 = a * kCorps * scaleFactor;
        
        float x2 = R1 * cos(a + da) * scaleFactor;
        float y2 = R1 * sin(a + da) * scaleFactor;
        float z2 = (a + da) * kCorps * scaleFactor;
        
        float x3 = R1 * cos(a) * scaleFactor;
        float y3 = R1 * sin(a) * scaleFactor;
        float z3 = z1 + offset * scaleFactor;
        
        float x4 = R1 * cos(a + da) * scaleFactor;
        float y4 = R1 * sin(a + da) * scaleFactor;
        float z4 = z2 + offset * scaleFactor;
        
        beginShape(QUAD_STRIP);
          vertex(x1, y1, z1);
          vertex(x2, y2, z2);
          vertex(x3, y3, z3);
          vertex(x4, y4, z4);
        endShape();
      }
    }
    // Tête : la translation en Z est ajustée pour rester collée au corps
    void tete() {
          float kTete = 10;
  pushMatrix();
    translate(0, 0, 300 * bodyThinness);
    
    float da = 4 * PI / 100;
    int nb_de_tours = 10;
    int iter_i = nb_de_tours * 50 / 2;
    
    for (int i = -iter_i; i <= iter_i; i++) {
      float a = i / 50.f * 2 * PI;
      float t = map(i, -iter_i, iter_i, -1, 1);
      float R1 = (150 + 100 * (1 - t * t)) * bodyThinness;
      float offset = R1 * offsetFactor;
      
      float x1 = R1 * cos(a) * headScaleFactor;
      float y1 = R1 * sin(a) * headScaleFactor;
      float z1 = a * kTete * headScaleFactor;
      
      float x2 = R1 * cos(a + da) * headScaleFactor;
      float y2 = R1 * sin(a + da) * headScaleFactor;
      float z2 = (a + da) * kTete * headScaleFactor;
      
      float x3 = R1 * cos(a) * headScaleFactor;
      float y3 = R1 * sin(a) * headScaleFactor;
      float z3 = z1 + offset * headScaleFactor;
      
      float x4 = R1 * cos(a + da) * headScaleFactor;
      float y4 = R1 * sin(a + da) * headScaleFactor;
      float z4 = z2 + offset * headScaleFactor;
      
      beginShape(QUAD_STRIP);
        vertex(x1, y1, z1);
        vertex(x2, y2, z2);
        vertex(x3, y3, z3);
        vertex(x4, y4, z4);
      endShape();
    }
    
    float eyeFactor = pow(bodyThinness, 0.8f);
    yeux(40 * headScaleFactor * eyeFactor,
         50 * headScaleFactor * eyeFactor,
         60 * headScaleFactor * eyeFactor);
  popMatrix();
    }
    // Yeux et pupilles : ici on utilise eyeFactor pour atténuer la diminution
    void yeux(float eyeX, float eyeY, float eyeZ) {
  float eyeFactor = pow(bodyThinness, 0.8f);
  float eyeRadius = 185 * headScaleFactor * eyeFactor;
  
  pushMatrix();
    translate(eyeX, eyeY, eyeZ);
    fill(255);
    noStroke();
    sphere(eyeRadius);
  popMatrix();
  
  pushMatrix();
    translate(-eyeX, eyeY, eyeZ);
    fill(255);
    noStroke();
    sphere(eyeRadius);
  popMatrix();
  
  pupilles(50 * eyeFactor, 70 * eyeFactor, 25, 
           -50 * eyeFactor, 70 * eyeFactor, 25);
    }

    void pupilles(float pupilRightX, float pupilRightY, float pupilRightZ, 
                  float pupilLeftX, float pupilLeftY, float pupilLeftZ) {
  float eyeFactor = pow(bodyThinness, 0.8f);
  float pupilSizeFactor = 29 * eyeFactor;
  
  pushMatrix();
    translate(pupilRightX, pupilRightY, pupilRightZ);
    fill(0);
    noStroke();
    rotateZ(PI / 2);
    scale(pupilSizeFactor, pupilSizeFactor * 0.6f, pupilSizeFactor);
    sphere(1);
  popMatrix();
  
  pushMatrix();
    translate(pupilLeftX, pupilLeftY, pupilLeftZ);
    fill(0);
    noStroke();
    rotateZ(PI / 2);
    scale(pupilSizeFactor, pupilSizeFactor * 0.6f, pupilSizeFactor);
    sphere(1);
  popMatrix();
    }


    // Bras : le calcul du rayon intègre bodyThinness
    void bras(float brasPositionX, float brasPositionY, float brasPositionZ, float brasSize) {
          fill(112, 108, 17);
  float brasK = 15;
  float da = 4 * PI / 100;
  int nb_de_tours = 10;
  int iter_i = nb_de_tours * 50 / 2;
  
  pushMatrix();
    translate(brasPositionX, brasPositionY, brasPositionZ);
    scale(brasSize);
    rotateX(HALF_PI);
    
    for (int i = -iter_i; i <= iter_i; i++) {
      float a = i / 50.f * 2 * PI;
      float t = map(i, -iter_i, iter_i, -1, 1);
      float R1 = (150 + 100 * (1 - t * t)) * bodyThinness;
      float offset = R1 * offsetFactor;
      
      float x1 = R1 * cos(a) * scaleFactor;
      float y1 = R1 * sin(a) * scaleFactor;
      float z1 = a * brasK * scaleFactor;
      
      float x2 = R1 * cos(a + da) * scaleFactor;
      float y2 = R1 * sin(a + da) * scaleFactor;
      float z2 = (a + da) * brasK * scaleFactor;
      
      float x3 = R1 * cos(a) * scaleFactor;
      float y3 = R1 * sin(a) * scaleFactor;
      float z3 = z1 + offset * scaleFactor;
      
      float x4 = R1 * cos(a + da) * scaleFactor;
      float y4 = R1 * sin(a + da) * scaleFactor;
      float z4 = z2 + offset * scaleFactor;
      
      beginShape(QUAD_STRIP);
        vertex(x1, y1, z1);
        vertex(x2, y2, z2);
        vertex(x3, y3, z3);
        vertex(x4, y4, z4);
      endShape();
    }
  popMatrix();
    }

    // Ajouter les mains : on applique bodyThinness sur les offsets horizontaux et sur la taille
    void ajouterMain(float brasPositionX, float brasPositionY, float brasPositionZ, 
                     float brasSize, boolean isLeftHand,
                     float offsetX, float offsetY, float offsetZ) {
          if (!handsLoaded) return;
  
  float brasK = 15;
  
  pushMatrix();
    translate(brasPositionX, brasPositionY, brasPositionZ);
    scale(brasSize);
    rotateX(0);
    rotateY(PI);
    
    float longueurBras = brasK * PI * 2 * scaleFactor;
    translate(0, 0, longueurBras);
    rotateX(-HALF_PI);
    translate(offsetX * bodyThinness, offsetY, offsetZ * bodyThinness);
    
    float handScale = 30 * bodyThinness;
    scale(handScale);
    
    if (isLeftHand) {
      shape(hand1);
    } else {
      shape(hand2);
    }
  popMatrix();
    }
} 
