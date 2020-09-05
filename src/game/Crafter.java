package game;

import engine.*;
import engine.graph.Camera;
import engine.sound.SoundBuffer;
import engine.sound.SoundListener;
import engine.sound.SoundManager;
import engine.sound.SoundSource;
import game.player.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

import static engine.Chunk.genBiome;
import static engine.Chunk.initializeChunkHandler;
import static engine.ItemEntity.clearItems;
import static engine.TNTEntity.createTNTEntityMesh;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.blocks.BlockDefinition.initializeBlocks;
import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    public static int chunkRenderDistance = 20;

    private static final float MOUSE_SENSITIVITY = 0.009f;

    private final Renderer renderer;

    private final SoundManager soundMgr;

    private final Camera camera;

    private boolean fButtonPushed = false;

    private boolean rButtonPushed = false;

    private boolean tButtonPushed = false;

    private boolean cButtonPushed = false;

    private boolean boomBuffer = false;

    private Player player;

    public enum Sounds { TNT, STONE, TNTHISS, PICKUP };

    public static int getChunkRenderDistance(){
        return chunkRenderDistance;
    }

    public Crafter(){
        renderer = new Renderer();
        camera = new Camera();
        soundMgr = new SoundManager();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);

        soundMgr.init();

        //this initializes the block definitions
        initializeBlocks(soundMgr);

        //this creates arrays for the engine to handle the objects
        initializeChunkHandler(chunkRenderDistance);

        //this creates a TNT mesh (here for now)
        createTNTEntityMesh();

        //create the initial map in memory
        int x = -chunkRenderDistance;
        int z = -chunkRenderDistance;
        for (int i = 0; i < ((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1); i++){
            genBiome(x,z);
            x++;
            if (x > chunkRenderDistance){
                x = -chunkRenderDistance;
                z++;
            }
        }

        //create the initial map in memory
        x = -chunkRenderDistance;
        z = -chunkRenderDistance;
        for (int i = 0; i < ((chunkRenderDistance * 2) + 1) * ((chunkRenderDistance * 2) + 1); i++){
            generateChunkMesh(x, z, false);
            System.out.println(x + " " + z);
            x++;
            if (x > chunkRenderDistance){
                x = -chunkRenderDistance;
                z++;
            }
        }
        player = new Player("singleplayer");

        this.soundMgr.init();
        this.soundMgr.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
        setupSounds();
    }

    private void setupSounds() throws Exception {

        SoundBuffer buffBack = new SoundBuffer("sounds/stone_1.ogg");
        soundMgr.addSoundBuffer(buffBack);
        SoundSource sourceBack = new SoundSource(false, false);
        sourceBack.setBuffer(buffBack.getBufferId());
        soundMgr.addSoundSource(Sounds.STONE.toString(), sourceBack);

        SoundBuffer buffBack2 = new SoundBuffer("sounds/tnt_explode.ogg");
        soundMgr.addSoundBuffer(buffBack2);
        SoundSource sourceBack2 = new SoundSource(false, false);
        sourceBack2.setBuffer(buffBack2.getBufferId());
        soundMgr.addSoundSource(Sounds.TNT.toString(), sourceBack2);

        SoundBuffer buffBack3 = new SoundBuffer("sounds/tnt_ignite.ogg");
        soundMgr.addSoundBuffer(buffBack3);
        SoundSource sourceBack3 = new SoundSource(false, false);
        sourceBack3.setBuffer(buffBack3.getBufferId());
        soundMgr.addSoundSource(Sounds.TNTHISS.toString(), sourceBack3);


        SoundBuffer buffBack4 = new SoundBuffer("sounds/pickup.ogg");
        soundMgr.addSoundBuffer(buffBack4);
        SoundSource sourceBack4 = new SoundSource(false, false);
        sourceBack4.setBuffer(buffBack4.getBufferId());
        soundMgr.addSoundSource(Sounds.PICKUP.toString(), sourceBack4);
//
        soundMgr.setListener(new SoundListener(new Vector3f()));
    }

    @Override
    public void input(Window window, MouseInput input){
//        System.out.println("input thread is running");
        if (window.isKeyPressed(GLFW_KEY_W)){
            player.setForward(true);
        } else {
            player.setForward(false);
        }

        if (window.isKeyPressed(GLFW_KEY_S)){
            player.setBackward(true);
        } else{
            player.setBackward(false);
        }
        if (window.isKeyPressed(GLFW_KEY_A)){
            player.setLeft(true);
        } else {
            player.setLeft(false);
        }
        if (window.isKeyPressed(GLFW_KEY_D)){
            player.setRight(true);
        } else {
            player.setRight(false);
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){ //sneaking
            player.setSneaking(true);
        }else{
            player.setSneaking(false);
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)){
            player.setJump(true);
        } else {
            player.setJump(false);
        }

        //prototype toggle locking mouse - F KEY
        if (window.isKeyPressed(GLFW_KEY_F)) {
            if (!fButtonPushed) {
                input.setMouseLocked(!input.isMouseLocked());
                fButtonPushed = true;
                if(!input.isMouseLocked()) {
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                } else{
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_F)){
            fButtonPushed = false;
        }

        //prototype reset position - R KEY
        if (window.isKeyPressed(GLFW_KEY_R)) {
            if (!rButtonPushed) {
                rButtonPushed = true;
                player.setPos(new Vector3f(0,129,0));

                if (Math.random() == 0.0001f){
                    System.out.println("Hey, Ben!");
                }
                else {
                    System.out.println("Position reset!");
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_R)){
            rButtonPushed = false;
        }

        //prototype clear objects - C KEY
        if (window.isKeyPressed(GLFW_KEY_C)) {
            if (!cButtonPushed) {
                cButtonPushed = true;
                clearItems();
                System.out.println("Cleared all items!");
            }
        } else if (!window.isKeyPressed(GLFW_KEY_C)){
            cButtonPushed = false;
        }

        //mouse left button input
        if(input.isLeftButtonPressed()){
            player.setMining(true);
        } else {
            player.setMining(false);
        }

        //mouse right button input
        if(input.isRightButtonPressed()){
            player.setPlacing(true);
        } else {
            player.setPlacing(false);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {

        camera.setPosition(player.getPosWithEyeHeight().x, player.getPosWithEyeHeight().y, player.getPosWithEyeHeight().z);
        camera.movePosition(player.getViewBobbing().x,player.getViewBobbing().y, player.getViewBobbing().z);

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

        player.onTick(camera, soundMgr);

        ItemEntity.onStep(soundMgr);

        TNTEntity.onTNTStep(soundMgr);

//        soundMgr.updateListenerPosition(camera);
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
