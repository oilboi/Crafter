package game;

import engine.ChunkObject;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import game.ChunkHandling.Chunk;
import game.player.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static game.ChunkHandling.ChunkData.storeChunk;
import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static game.blocks.BlockDefinition.initializeBlocks;
import static game.player.TNT.boom;
import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    public static int chunkRenderDistance = 3;

    private static final float MOUSE_SENSITIVITY = 0.008f;

    private final Renderer renderer;

    private final Camera camera;

    private static ChunkObject[][] mapMeshes = new ChunkObject[(chunkRenderDistance*2)+1][(chunkRenderDistance*2)+1];

    private boolean fButtonPushed = false;
//
    private boolean rButtonPushed = false;
//
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

        //create the initial map
        for (int x = -chunkRenderDistance; x <= chunkRenderDistance; x++) {
            for (int z = -chunkRenderDistance; z <= chunkRenderDistance; z++) {
//                System.out.println("-------");
                System.out.println(x + " " + z);
                Chunk thisChunk = new Chunk(x, z);
                storeChunk(x,z, thisChunk);
                generateChunkMesh(x, z, mapMeshes, false);
            }
        }

        player = new Player();
    }

    private static final float inertiaApplicationDivision = 1f;
    @Override
    public void input(Window window, MouseInput input){


        if (window.isKeyPressed(GLFW_KEY_W) && !player.getForward()){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)Math.PI;
            float x = (float)Math.sin(-yaw) / inertiaApplicationDivision;
            float z = (float)Math.cos(yaw) / inertiaApplicationDivision;
            player.setInertiaBuffer(x,0,z);
            player.setForward();
        }
        if (window.isKeyPressed(GLFW_KEY_S) && !player.getBackward()){
            //no mod needed
            float yaw = (float)Math.toRadians(camera.getRotation().y);
            float x = (float)Math.sin(-yaw) / inertiaApplicationDivision;
            float z = (float)Math.cos(yaw) / inertiaApplicationDivision;
            player.setInertiaBuffer(x,0,z);
            player.setBackward();
        }
        if (window.isKeyPressed(GLFW_KEY_A) && !player.getLeft()){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)(Math.PI /2);
            float x = (float)Math.sin(-yaw) / inertiaApplicationDivision;
            float z = (float)Math.cos(yaw) / inertiaApplicationDivision;
            player.setInertiaBuffer(x,0,z);
            player.setLeft();
        }
        if (window.isKeyPressed(GLFW_KEY_D) && !player.getRight()){
            float yaw = (float)Math.toRadians(camera.getRotation().y) - (float)(Math.PI /2);
            float x = (float)Math.sin(-yaw) / inertiaApplicationDivision;
            float z = (float)Math.cos(yaw) / inertiaApplicationDivision;
            player.setInertiaBuffer(x,0,z);
            player.setRight();
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            //cameraInc.y = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE) && player.isOnGround() && !player.getJump()){
            player.setInertiaBuffer(0,12f,0);
            player.setJump();
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
                System.out.println("Position reset!");
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
//
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

        player.onTick(camera, mapMeshes);

        if(boomBuffer){
            boom((int)Math.floor(player.getPos().x),(int)Math.floor(player.getPos().y),(int)Math.floor(player.getPos().z), mapMeshes);
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

    }

    @Override
    public void render(Window window){
        renderer.render(window, camera, mapMeshes);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for(Object[] chunkMeshArray : mapMeshes){
            if (chunkMeshArray == null){
                continue;
            }
            for (Object chunkMesh : chunkMeshArray) {
                if(chunkMesh == null){
                    continue;
                }
                ((ChunkObject) chunkMesh).getMesh().cleanUp();
            }
        }
    }
}
