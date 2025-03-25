package processing;

public class Configs {
    // Singleton
    private static Configs instance = new Configs();

    // APP CONFIGS ---------------------------------------
    private boolean fullScreen;    // fullScreen overrides width and height
    private int screenWidth;
    private int screenHeight;
    private int fps;
    
    // MAZE CONFIGS ---------------------------------------
    private int CELL_SIZE;         // the size of each cell in the maze

    // PYRAMID CONFIGS ---------------------------------------
    private int PYRAMID_SIZE;       // the size of the bottom maze

    // CAMERA CONFIGS ---------------------------------------
    private float playerHeight;
    private float mouseSensitivity;
    private float moveSpeed;       // pixels per second

    private Configs() {
        // APP CONFIGS ---------------------------------------
        fullScreen = false;
        screenWidth = 1600;
        screenHeight = 900;
        fps = 60;

        // MAZE CONFIGS ---------------------------------------
        PYRAMID_SIZE = 21;
        CELL_SIZE = 40;

        // CAMERA CONFIGS ---------------------------------------
        playerHeight = CELL_SIZE;
        mouseSensitivity = 0.2f;
        moveSpeed = 60f;
    }
    public interface ConfigsReader {
        boolean getFullScreen();
        int getScreenWidth();
        int getScreenHeight();
        int getFps();                   // to get actual current fps, use frameRate from PApplet
        int getPyramidSize();
        int getCellSize();
        float getPlayerHeight();
        float getMouseSensitivity();
        float getMoveSpeed();
    }
    public interface ConfigsWriter {
        void setFullScreen(boolean fullScreen);
        void setScreenWidth(int screenWidth);
        void setScreenHeight(int screenHeight);
        void setSCreenResolution(int screenWidth, int screenHeight);
        void setPyramidSize(int mazeSize);
        void setCellSize(int cellSize);
        void setPlayerHeight(float playerHeight);
        void setMouseSensitivity(float mouseSensitivity);
        void setMoveSpeed(float moveSpeed);
    }
    // A LOT OF BOILERPLATES
    public static ConfigsReader getReader() {
        return new ConfigsReader() {
            @Override
            public boolean getFullScreen() { return instance.fullScreen; }
            
            @Override
            public int getScreenWidth() { return instance.screenWidth; }
        
            @Override
            public int getScreenHeight() { return instance.screenHeight; }

            @Override
            public int getFps() { return instance.fps; }

            @Override
            public int getPyramidSize() { return instance.PYRAMID_SIZE; }

            @Override
            public int getCellSize() { return instance.CELL_SIZE; }

            @Override
            public float getPlayerHeight() { return instance.playerHeight; }

            @Override
            public float getMouseSensitivity() { return instance.mouseSensitivity; }

            @Override
            public float getMoveSpeed() { return instance.moveSpeed; }

         };
    }
    public static ConfigsWriter getWriter() {
        return new ConfigsWriter() {
            @Override
            public void setFullScreen(boolean fullScreen) { instance.fullScreen = fullScreen; }
            
            @Override
            public void setScreenWidth(int screenWidth) { 
                if (screenWidth <= 0) {
                    throw new IllegalArgumentException("Screen width must be greater than 0");
                }
                instance.screenWidth = screenWidth; 
            }
        
            @Override
            public void setScreenHeight(int screenHeight) { 
                if (screenHeight <= 0) {
                    throw new IllegalArgumentException("Screen height must be greater than 0");
                }
                instance.screenHeight = screenHeight; 
            }

            @Override 
            public void setSCreenResolution(int screenWidth, int screenHeight) {
                setScreenWidth(screenWidth);
                setScreenHeight(screenHeight);
            }

            @Override
            public void setPyramidSize(int mazeSize) { instance.PYRAMID_SIZE = mazeSize; }

            @Override
            public void setCellSize(int cellSize) { instance.CELL_SIZE = cellSize; }

            @Override
            public void setPlayerHeight(float playerHeight) { instance.playerHeight = playerHeight; }

            @Override
            public void setMouseSensitivity(float mouseSensitivity) { instance.mouseSensitivity = mouseSensitivity; }

            @Override
            public void setMoveSpeed(float moveSpeed) { instance.moveSpeed = moveSpeed; }

          };
    }
}
