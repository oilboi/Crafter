package game;

import engine.*;
import engine.graph.Camera;
import engine.sound.SoundListener;
import engine.sound.SoundManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

import static engine.Chunk.genBiome;
import static engine.Chunk.initializeChunkHandler;
import static engine.ChunkUpdateHandler.chunkUpdater;
import static engine.Hud.*;
import static engine.ItemEntity.initializeItemTextureAtlas;
import static engine.TNTEntity.createTNTEntityMesh;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.ChunkHandling.ChunkMesh.initializeChunkTextureAtlas;
import static game.blocks.BlockDefinition.initializeBlocks;
import static game.player.Player.*;
import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    public static int chunkRenderDistance = 2;

    private static final float MOUSE_SENSITIVITY = 0.009f;

    private final Renderer renderer;

    private static final SoundManager soundMgr = new SoundManager();

    private final Camera camera;

    private boolean fButtonPushed = false;

    private boolean rButtonPushed = false;

    private boolean tButtonPushed = false;

    private boolean cButtonPushed = false;

    private boolean eButtonPushed = false;

    public static int getChunkRenderDistance(){
        return chunkRenderDistance;
    }

    public Crafter(){
        renderer = new Renderer();
        camera = new Camera();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);

        soundMgr.init();

        initializeChunkTextureAtlas();
        initializeItemTextureAtlas();
        initializeFontTextureAtlas();

        //this initializes the block definitions
        initializeBlocks(soundMgr);

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

        this.soundMgr.init();
        this.soundMgr.setAttenuationModel(AL11.AL_LINEAR_DISTANCE);

        soundMgr.setListener(new SoundListener(new Vector3f()));

        createHud();
    }

    public static SoundManager getSoundManager(){
        return soundMgr;
    }

    @Override
    public void input(Window window, MouseInput input){


        if (!isPlayerInventoryOpen()) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                setPlayerForward(true);
            } else {
                setPlayerForward(false);
            }

            if (window.isKeyPressed(GLFW_KEY_S)) {
                setPlayerBackward(true);
            } else {
                setPlayerBackward(false);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                setPlayerLeft(true);
            } else {
                setPlayerLeft(false);
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                setPlayerRight(true);
            } else {
                setPlayerRight(false);
            }

            if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) { //sneaking
                setPlayerSneaking(true);
            } else {
                setPlayerSneaking(false);
            }

            if (window.isKeyPressed(GLFW_KEY_SPACE)) {
                setPlayerJump(true);
            } else {
                setPlayerJump(false);
            }
        }

        //prototype toggle locking mouse - F KEY
//        if (window.isKeyPressed(GLFW_KEY_F)) {
//            if (!fButtonPushed) {
//                input.setMouseLocked(!input.isMouseLocked());
//                fButtonPushed = true;
//                if(!input.isMouseLocked()) {
//                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
//                } else{
//                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
//                }
//            }
//        } else if (!window.isKeyPressed(GLFW_KEY_F)){
//            fButtonPushed = false;
//        }

        //prototype reset position - R KEY
//        if (window.isKeyPressed(GLFW_KEY_R)) {
//            if (!rButtonPushed) {
//                rButtonPushed = true;
//                player.setPos(new Vector3f(-48,57,-128));
//
//                if (Math.random() == 0.0001f){
//                    System.out.println("Hey, Ben!");
//                }
//                else {
//                    System.out.println("Position reset!");
//                }
//            }
//        } else if (!window.isKeyPressed(GLFW_KEY_R)){
//            rButtonPushed = false;
//        }

        //prototype clear objects - C KEY
//        if (window.isKeyPressed(GLFW_KEY_C)) {
//            if (!cButtonPushed) {
//                cButtonPushed = true;
//                clearItems();
//                System.out.println("Cleared all items!");
//            }
//        } else if (!window.isKeyPressed(GLFW_KEY_C)){
//            cButtonPushed = false;
//        }

        //prototype clear objects - C KEY
        if (window.isKeyPressed(GLFW_KEY_E)) {
            if (!eButtonPushed) {
                eButtonPushed = true;
                togglePlayerInventory();
                input.setMouseLocked(!input.isMouseLocked());
                if(!input.isMouseLocked()) {
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                } else{
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                    input.resetPosVector(window);
                }

                setPlayerForward(false);
                setPlayerBackward(false);
                setPlayerLeft(false);
                setPlayerRight(false);
                setPlayerSneaking(false);
                setPlayerJump(false);
            }
        } else if (!window.isKeyPressed(GLFW_KEY_E)){
            eButtonPushed = false;
        }


        if (!isPlayerInventoryOpen()) {
            //mouse left button input
            if (input.isLeftButtonPressed()) {
                setPlayerMining(true);
            } else {
                setPlayerMining(false);
            }

            //mouse right button input
            if (input.isRightButtonPressed()) {
                setPlayerPlacing(true);
            } else {
                setPlayerPlacing(false);
            }

            float scroll = input.getScroll();
            if (scroll < 0) {
                changeScrollSelection(1);
            } else if (scroll > 0) {
                changeScrollSelection(-1);
            }
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {

        chunkUpdater();

        camera.setPosition(getPlayerPosWithEyeHeight().x, getPlayerPosWithEyeHeight().y, getPlayerPosWithEyeHeight().z);
        camera.movePosition(getPlayerViewBobbing().x,getPlayerViewBobbing().y, getPlayerViewBobbing().z);

        //update camera based on mouse

        Vector2f rotVec = mouseInput.getDisplVec();

        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        //limit camera pitch
        if (camera.getRotation().x < -90f) {
            camera.moveRotation((90f + camera.getRotation().x) * -1f, 0, 0);
        }
        if (camera.getRotation().x > 90f){
            camera.moveRotation((camera.getRotation().x - 90f) * -1f , 0, 0);
        }

        //loop camera yaw
        if (camera.getRotation().y < -180f){
            camera.moveRotation(0,360, 0);
        }
        if (camera.getRotation().y > 180f){
            camera.moveRotation(0,-360, 0);
        }

        playerOnTick(camera);

        soundMgr.updateListenerPosition(camera);

        ItemEntity.onStep(soundMgr);

        TNTEntity.onTNTStep(soundMgr);

        hudOnStepTest();
    }

    @Override
    public void render(Window window){
        renderer.render(window, camera);
    }

    @Override
    public void cleanup(){
        Chunk.cleanUp();
        soundMgr.cleanup();
        ItemEntity.cleanUp();
        renderer.cleanup();
    }
}
