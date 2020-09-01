package game;

import engine.*;
import engine.graph.Camera;
import game.player.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.Chunk.genBiome;
import static engine.Chunk.initializeChunkHandler;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.blocks.BlockDefinition.initializeBlocks;
import static game.player.TNT.boom;
import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    public static int chunkRenderDistance = 2;

    private static final float MOUSE_SENSITIVITY = 0.009f;

    private final Renderer renderer;

    private final Camera camera;

    private boolean fButtonPushed = false;

    private boolean rButtonPushed = false;

    private boolean tButtonPushed = false;

    private boolean boomBuffer = false;

    private Player player;

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

        //this initializes the block definitions
        initializeBlocks();

        //this creates arrays for the engine to handle the objects
        initializeChunkHandler(chunkRenderDistance);

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
//
//
        //prototype explosion - T KEY
        if (window.isKeyPressed(GLFW_KEY_T)) {
            if (!tButtonPushed) {
                tButtonPushed = true;
                boomBuffer = true;
                System.out.println("boom");
            }
        } else if (!window.isKeyPressed(GLFW_KEY_T)){
            tButtonPushed = false;
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

        player.onTick(camera);

        if(boomBuffer){
            boom((int)Math.floor(player.getPos().x),(int)Math.floor(player.getPos().y),(int)Math.floor(player.getPos().z));
            boomBuffer = false;
        }

//        for (GameItem itemEntity : itemEntities){
//            Vector3f pos = itemEntity.getPosition();
//            Vector3f rot = itemEntity.getRotation();
//            rot.y += 0.1f;
//            itemEntity.setRotation(rot.x,rot.y,rot.z);
//
//            applyInertia(pos,new Vector3f(0,-4,0),false, 0.2f,0.4f, true);
//
//            itemEntity.setPosition(pos.x,pos.y,pos.z);
//        }

        ItemEntity.onStep();
    }

    @Override
    public void render(Window window){
        renderer.render(window, camera);
    }

    @Override
    public void cleanup(){
        Chunk.cleanUp();
        ItemEntity.cleanUp();
        renderer.cleanup();
    }
}
