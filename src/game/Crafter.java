package game;

import engine.*;
import engine.Window;
import engine.graph.Camera;
import engine.sound.SoundListener;
import engine.sound.SoundManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

import java.awt.*;

import static engine.Chunk.genBiome;
import static engine.Chunk.initializeChunkHandler;
import static engine.ChunkUpdateHandler.chunkUpdater;
import static engine.Hud.*;
import static engine.ItemEntity.initializeItemTextureAtlas;
import static engine.MouseInput.*;
import static engine.TNTEntity.createTNTEntityMesh;
import static engine.Window.*;
import static engine.graph.Camera.*;
import static engine.sound.SoundManager.*;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.ChunkHandling.ChunkMesh.initializeChunkTextureAtlas;
import static game.Renderer.*;
import static game.blocks.BlockDefinition.initializeBlocks;
import static game.player.Inventory.generateRandomInventory;
import static game.player.Player.*;
import static org.lwjgl.glfw.GLFW.*;

public class Crafter {

    //variables
    private static int     chunkRenderDistance = 2;
    private static float   MOUSE_SENSITIVITY   = 0.009f;
    private static boolean fButtonPushed       = false;
    private static boolean rButtonPushed       = false;
    private static boolean tButtonPushed       = false;
    private static boolean cButtonPushed       = false;
    private static boolean eButtonPushed       = false;

    //core game engine elements
    private static final int TARGET_FPS = 75;
    private static final int TARGET_UPS = 60; //TODO: IMPLEMENT THIS PROPERLY
    private static Timer timer;

    public static void main(String[] args){
        try{
            boolean vSync = true;
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension d = tk.getScreenSize();
            runGameEngine("Crafter", d.width/2,d.height/2,false);
        } catch ( Exception excp ){
            excp.printStackTrace();
            System.exit(-1);
        }
    }

    //the game engine elements //todo ------------------------------------------------------------------------------------ START

    public static void runGameEngine(String windowTitle, int width, int height, boolean vSync){
        try{
            initWindow(windowTitle, width, height, vSync);
            initTheGame();
            gameLoop();
        } catch (Exception excp){
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void initTheGame() throws Exception{

        timer = new Timer();
        timer.init();
        initMouseInput();
        initRenderer();
        initGame();
    }

    private static void gameLoop() throws Exception {
        double elapsedTime;
        double accumulator = 0d;
//        float interval = 1f / TARGET_UPS;
        boolean running = true;
        while(running && !windowShouldClose()){

            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= 1_000_000){

                update(0f);

                accumulator -= 1_000_000;
            }

            renderGame();

            if (isvSync()){
                sync();
            }
        }
    }

    private static void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while(timer.getTime() < endTime){
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie){
            }
        }
    }
    //todo ---------------------------------------------------------------------------------------------------------------END



    public static int getChunkRenderDistance(){
        return chunkRenderDistance;
    }

    public static void initGame() throws Exception{
        initSoundManager();

        initializeChunkTextureAtlas();
        initializeItemTextureAtlas();
        initializeFontTextureAtlas();

        //this initializes the block definitions
        initializeBlocks();

        //this creates arrays for the engine to handle the objects
        initializeChunkHandler(chunkRenderDistance);

        //this creates a TNT mesh (here for now)
        createTNTEntityMesh();

        //create the initial map in memory
        int x;
        int z;
        for (x = -chunkRenderDistance; x <= chunkRenderDistance; x++){
            for (z = -chunkRenderDistance; z<= chunkRenderDistance; z++){
                genBiome(x,z);
            }
        }

        //create chunk meshes
        for (x = -chunkRenderDistance; x <= chunkRenderDistance; x++){
            for (z = -chunkRenderDistance; z<= chunkRenderDistance; z++){
                generateChunkMesh(x, z, false);
            }
        }

        setAttenuationModel(AL11.AL_LINEAR_DISTANCE);
        setListener(new SoundListener(new Vector3f()));

        createHud();

        generateRandomInventory();
    }

    private static void input(){


        if (!isPlayerInventoryOpen()) {
            if (isKeyPressed(GLFW_KEY_W)) {
                setPlayerForward(true);
            } else {
                setPlayerForward(false);
            }

            if (isKeyPressed(GLFW_KEY_S)) {
                setPlayerBackward(true);
            } else {
                setPlayerBackward(false);
            }
            if (isKeyPressed(GLFW_KEY_A)) {
                setPlayerLeft(true);
            } else {
                setPlayerLeft(false);
            }
            if (isKeyPressed(GLFW_KEY_D)) {
                setPlayerRight(true);
            } else {
                setPlayerRight(false);
            }

            if (isKeyPressed(GLFW_KEY_LEFT_SHIFT)) { //sneaking
                setPlayerSneaking(true);
            } else {
                setPlayerSneaking(false);
            }

            if (isKeyPressed(GLFW_KEY_SPACE)) {
                setPlayerJump(true);
            } else {
                setPlayerJump(false);
            }
        }

        if (isKeyPressed(GLFW_KEY_R)) {
            if (!rButtonPushed) {
                rButtonPushed = true;
                generateRandomInventory();
            }
        } else if (!isKeyPressed(GLFW_KEY_R)){
            rButtonPushed = false;
        }


        //prototype clear objects - C KEY
        if (isKeyPressed(GLFW_KEY_E)) {
            if (!eButtonPushed) {
                eButtonPushed = true;
                togglePlayerInventory();
                setMouseLocked(!isMouseLocked());
                if(!isMouseLocked()) {
                    glfwSetInputMode(getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                } else{
                    glfwSetInputMode(getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                    resetMousePosVector();
                }

                setPlayerForward(false);
                setPlayerBackward(false);
                setPlayerLeft(false);
                setPlayerRight(false);
                setPlayerSneaking(false);
                setPlayerJump(false);
            }
        } else if (!isKeyPressed(GLFW_KEY_E)){
            eButtonPushed = false;
        }


        if (!isPlayerInventoryOpen()) {
            //mouse left button input
            if (isLeftButtonPressed()) {
                setPlayerMining(true);
            } else {
                setPlayerMining(false);
            }

            //mouse right button input
            if (isRightButtonPressed()) {
                setPlayerPlacing(true);
            } else {
                setPlayerPlacing(false);
            }

            float scroll = getMouseScroll();
            if (scroll < 0) {
                changeScrollSelection(1);
            } else if (scroll > 0) {
                changeScrollSelection(-1);
            }
        }
    }

    private static void update(float interval) throws Exception {

        chunkUpdater();

        setCameraPosition(getPlayerPosWithEyeHeight().x, getPlayerPosWithEyeHeight().y, getPlayerPosWithEyeHeight().z);
        moveCameraPosition(getPlayerViewBobbing().x,getPlayerViewBobbing().y, getPlayerViewBobbing().z);

        //update camera based on mouse

        Vector2f rotVec = getMouseDisplVec();

        moveCameraRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        //limit camera pitch
        if (getCameraRotation().x < -90f) {
            moveCameraRotation((90f + getCameraRotation().x) * -1f, 0, 0);
        }
        if (getCameraRotation().x > 90f){
            moveCameraRotation((getCameraRotation().x - 90f) * -1f , 0, 0);
        }

        //loop camera yaw
        if (getCameraRotation().y < -180f){
            moveCameraRotation(0,360, 0);
        }
        if (getCameraRotation().y > 180f){
            moveCameraRotation(0,-360, 0);
        }

        playerOnTick();
        updateListenerPosition();
        ItemEntity.onStep();
        TNTEntity.onTNTStep();

        hudOnStepTest();
    }

    private static void render(){
        renderGame();
    }

    private static void cleanup(){
        Chunk.cleanUp();
        cleanupSoundManager();
        ItemEntity.cleanUp();
        cleanupRenderer();
    }
}
